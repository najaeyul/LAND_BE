package land.land_be.controller.member

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import land.land_be.config.security.jwt.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
class MemberController(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("token/expired")
    fun auth(): String {
        throw RuntimeException()
    }

    @GetMapping("token/refresh")
    fun refreshAuth(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<HttpStatus> {
        val token = request.getHeader("X-REFRESH-TOKEN")

        if (token != null && jwtTokenProvider.validateToken(token)) {
            val email = jwtTokenProvider.getUserPk(token)
            val newToken = jwtTokenProvider.generateToken(email, "Member")

            response.addHeader("X-AUTH-TOKEN", newToken.authToken)
            response.addHeader("X-REFRESH-TOKEN", newToken.refreshToken)
            response.contentType = "application/json;charset=UTF-8"

            return ResponseEntity.ok(HttpStatus.OK)
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED)
        }
    }
}