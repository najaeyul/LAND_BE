package land.land_be.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import land.land_be.config.security.jwt.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
class MemberController(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping("/token/expired")
    fun auth(): String {
        throw RuntimeException()
    }

    @GetMapping("/token/refresh")
    fun refreshAuth(request: HttpServletRequest, response: HttpServletResponse): String {
        val token = request.getHeader("Refresh")

        if (token != null && jwtTokenProvider.validateToken(token)) {
            val email = jwtTokenProvider.getUserPk(token)
            val newToken = jwtTokenProvider.generateToken(email, "Member")

            response.addHeader("X-AUTH-TOKEN", newToken.token)
            response.addHeader("X-REFRESH-TOKEN", newToken.refreshToken)
            response.contentType = "application/json;charset=UTF-8"

            return "HAPPY NEW TOKEN"
        }

        throw RuntimeException()
    }
}