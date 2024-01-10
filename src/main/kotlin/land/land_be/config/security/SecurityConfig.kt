package land.land_be.config.security

import land.land_be.config.security.oauth2.CustomOAuth2UserService
import land.land_be.config.security.oauth2.OAuth2SuccessHandler
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*


@Component
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val successHandler: OAuth2SuccessHandler,
    private val corsConfigurationSource: CorsConfigurationSource,
    private val securityHandlerConfig: SecurityHandlerConfig
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .headers { headers -> headers.httpStrictTransportSecurity { hsts -> hsts.disable() } }
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource)
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/api/**", "/login/**", "/oauth2/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling { exception ->
                exception.accessDeniedHandler(securityHandlerConfig.accessDeniedHandler())
                exception.authenticationEntryPoint(securityHandlerConfig.authenticationEntryPoint())
            }
            .csrf { csrf ->
                csrf.disable()
            }
            .sessionManagement { sessionManagement ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .successHandler(successHandler)
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService)
                    }
            }
            //.addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            .httpBasic(withDefaults())
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:3001")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.allowCredentials = true

        configuration.exposedHeaders = listOf(
            "Access-Control-Allow-Headers",
            "X-REFRESH-TOKEN, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                    "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
        )

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}