package land.land_be.config.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import land.land_be.dto.MemberDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val userDetailsService: UserDetailsService
) {

    @Value("\${jwt.secretKey}")
    lateinit var secretKey: String

    lateinit var key: SecretKey

    val tokenValidTime: Long = 60 * 1000L * 30 //30분
    val refreshTokenValidTime: Long = 60 * 1000L * 120 //2시간

    @PostConstruct
    fun init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256).also { secretKey = Base64.getEncoder().encodeToString(it.encoded) }
    }

    // token 생성
    fun generateToken(userPk: String, roles: String): MemberDto.TokenDto {
        var claims = Jwts.claims().setSubject(userPk)
        claims["roles"] = roles

        var now = Date()

        val token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + tokenValidTime))
            .signWith(key)
            .compact()
        val refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + refreshTokenValidTime))
            .signWith(key)
            .compact()

        return MemberDto.TokenDto(token, refreshToken)
    }

    // 인증정보조회
    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUserPk(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // 회원정보 추출
    fun getUserPk(token: String): String {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
    }

    // header에서 토큰값가져오기
    fun resolveToken(request: HttpServletRequest): String {
        return request.getHeader("X-AUTH-TOKEN")
    }

    // 토큰만료확인
    fun validateToken(token: String): Boolean {
        return try {
            val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}