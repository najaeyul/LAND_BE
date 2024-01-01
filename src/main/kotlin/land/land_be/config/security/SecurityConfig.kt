package land.land_be.config.security

import land.land_be.config.security.oauth2.CustomOAuth2ClientProperties
import land.land_be.config.security.oauth2.CustomOAuth2Provider
import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component


@Component
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "/login/**", "/oauth2/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
            }
            .httpBasic(withDefaults())
        return http.build()
    }

    @Bean
    fun clientRegistrationRepository(oAuth2ClientProperties: OAuth2ClientProperties,
                                     customOAuth2ClientProperties: CustomOAuth2ClientProperties
    ): InMemoryClientRegistrationRepository {

        // 소셜 설정 등록
        val registrations = oAuth2ClientProperties.registration.keys.mapNotNull {
            getRegistration(
                oAuth2ClientProperties,
                it
            )
        }
            .toMutableList()

        // 추가 설정 프로퍼티
        val customRegistrations = customOAuth2ClientProperties.registration

        // 추가 소셜 설정을 기본 소셜 설정에 추가
        if (customRegistrations != null) {
            for (customRegistration in customRegistrations) {

                when (customRegistration.key) {
                    "naver" -> registrations.add(
                        CustomOAuth2Provider.NAVER.getBuilder("naver")
                        .clientId(customRegistration.value.clientId)
                        .clientSecret(customRegistration.value.clientSecret)
                        .build())
                }

            }
        }

        return InMemoryClientRegistrationRepository(registrations)
    }

    // 공통 소셜 설정을 호출합니다.
    private fun getRegistration(clientProperties: OAuth2ClientProperties, client: String): ClientRegistration? {
        val registration = clientProperties.registration[client]
        return when(client) {
            "google" -> CommonOAuth2Provider.GOOGLE.getBuilder(client)
                .clientId(registration?.clientId)
                .clientSecret(registration?.clientSecret)
                .scope("email", "profile")
                .build()
            else -> null
        }
    }
}