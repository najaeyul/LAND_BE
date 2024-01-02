package land.land_be.config.security.oauth2

import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod


enum class CustomOAuth2Provider {

    NAVER {
        override fun getBuilder(registrationId: String): ClientRegistration.Builder =
            getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_POST, DEFAULT_LOGIN_REDIRECT_URL)
                .scope("profile")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://nid.naver.com/oauth2.0/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("response")
                .clientName("Naver")
    };

    /**
     * companion object: 코틀린에서는 static 키워드가 없음. 대신에, 클래스 내부에 companion object를 선언하여, 그 안에 정적 멤버(함수 또는 속성)를 선언. 이렇게 선언된 멤버들은 클래스의 인스턴스를 생성하지 않고도 직접 접근. 공유되어야 하는 값이나 상수, 유틸리티 함수 등을 정의하는데 사용
     * const: 컴파일 타임 상수를 정의할 때 사용. const val로 선언된 상수는 불변의 값을 가지며, 이 값은 컴파일 시점에 결정. 이러한 상수는 성능 최적화에 도움
     */
    companion object {
        const val DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}"
    }

    protected fun getBuilder(registrationId: String, method: ClientAuthenticationMethod, redirectUri: String): ClientRegistration.Builder =
        ClientRegistration.withRegistrationId(registrationId)
            .clientAuthenticationMethod(method)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(redirectUri)

    /**
     * 추상 메소드이므로 해당 enum 항목에서 오버라이드된 getBuilder(registrationId: String) 메소드가 실행
     * 필요성
     * 표준화: 추상 메소드를 사용하면 모든 서브 클래스가 같은 이름, 반환 타입, 매개 변수를 가진 메소드를 갖게 됩니다. 이는 코드의 일관성을 유지하고, 다양한 객체를 동일하게 처리할 수 있게 합니다.
     * 강제성: 추상 메소드는 서브 클래스에서 그 메소드를 반드시 구현하도록 강제합니다. 이를 통해 개발자가 특정 메소드를 무시하거나 잊어버리는 것을 방지할 수 있습니다.
     * 유연성: 추상 메소드는 서브 클래스마다 다른 구현을 가질 수 있습니다. 이는 상황에 따라 메소드의 동작을 변경할 수 있게 하여, 프로그램의 유연성을 높입니다
     */
    abstract fun getBuilder(registrationId: String): ClientRegistration.Builder
}