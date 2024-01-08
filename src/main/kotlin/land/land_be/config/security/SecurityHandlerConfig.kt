package land.land_be.config.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler

@Configuration
class SecurityHandlerConfig {

    @Bean
    fun accessDeniedHandler(): AccessDeniedHandler? {
        return AccessDeniedHandler { request: HttpServletRequest?, response: HttpServletResponse, e: AccessDeniedException? ->
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.contentType = "text/plain;charset=UTF-8"
            response.writer.write(
//                "{" +
//                        "\"detail\": \"" + ErrorCode.UNAUTHORIZED.getDetail() + "\"," +
//                        "\"code\": \"" + ErrorCode.UNAUTHORIZED.getCode() + "\"" +
//                        "}"
                "{" +
                        "\"detail\": \"" + "잘못된토큰" + "\"," +
                        "\"code\": \"" + "ㅎㅎ" + "\"" +
                        "}"
            )
            response.writer.flush()
            response.writer.close()
        }
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint? {
        return AuthenticationEntryPoint { request: HttpServletRequest?, response: HttpServletResponse, e: AuthenticationException? ->
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = "text/plain;charset=UTF-8"
            response.writer.write(
//                "{" +
//                        "\"detail\": \"" + ErrorCode.INVALID_TOKEN.getDetail() + "\"," +
//                        "\"code\": \"" + ErrorCode.INVALID_TOKEN.getCode() + "\"" +
//                        "}"
                "{" +
                        "\"detail\": \"" + "잘못된토큰" + "\"," +
                        "\"code\": \"" + "ㅎㅎ" + "\"" +
                        "}"
            )
            response.writer.flush()
            response.writer.close()
        }
    }
}