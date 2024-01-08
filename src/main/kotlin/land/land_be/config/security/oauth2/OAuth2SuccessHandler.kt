package land.land_be.config.security.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import land.land_be.config.security.jwt.JwtTokenProvider
import land.land_be.domain.member.JoinType
import land.land_be.domain.member.RoleType
import land.land_be.dto.member.MemberDto
import land.land_be.mapper.UserRequestMapper
import land.land_be.service.member.MemberService
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRequestMapper: UserRequestMapper,
    private val objectMapper: ObjectMapper,
    private val memberService: MemberService
) : AuthenticationSuccessHandler {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User
        val userDto = userRequestMapper.toDto(oAuth2User)

        // 최초 로그인이라면 회원가입 처리를 한다.
        if (!memberService.checkIfUserExists(userDto.id)) {
            val joinType = when (val registrationId = (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId) {
                "naver" -> JoinType.NAVER
                "google" -> JoinType.GOOGLE
                else -> throw IllegalArgumentException("Invalid registrationId: $registrationId")
            }

            val memberRequestDto = MemberDto.MemberRequestDto(
                id = userDto.id,
                email = userDto.email,
                name = userDto.name,
                gender = userDto.gender,
                birthYear = userDto.birthYear,
                joinType = joinType,
                role = RoleType.MEMBER
            )

            memberService.signUp(memberRequestDto)
        }

        // 토큰 발행
        val token = jwtTokenProvider.generateToken(userDto.email, "MEMBER")
        log.info("{}", token)

        writeTokenResponse(response, token)
    }

    /**
     * 토큰 작성
     */
    private fun writeTokenResponse(response: HttpServletResponse, token: MemberDto.TokenDto) {
        response.contentType = "text/html;charset=UTF-8"

        response.addHeader("X-AUTH-TOKEN", token.authToken)
        response.addHeader("X-REFRESH-TOKEN", token.refreshToken)
        response.contentType = "application/json;charset=UTF-8"

        response.writer.println(objectMapper.writeValueAsString(token))
        response.writer.flush()
    }
}