package land.land_be.config.security.oauth2

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration")
class CustomOAuth2ClientProperties {
    /**
     * lateinit : 코틀린에서 늦은 초기화(late initialization)를 위해 사용. non-null 타입의 프로퍼티를 선언할 때 초기값을 주지 않고, 나중에 초기화할 수 있게 해줌
     */
    var registration: Map<String, Registration>? = null
    // lateinit var registration: Map<String, Registration>

    companion object {
        class Registration {
            lateinit var clientId: String
            lateinit var clientSecret: String
        }
    }
}