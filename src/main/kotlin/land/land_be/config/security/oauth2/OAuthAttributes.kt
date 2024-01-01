package land.land_be.config.security.oauth2

data class OAuthAttributes(
    var attributes: Map<String, Any>,
    var nameAttributeKey: String,
    var email: String,
    var name: String,
    var gender: String,
    var birthYear: String
) {
    companion object {
        fun of(registrationId: String, userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return if (registrationId == "naver") {
                ofNaver("response", attributes)
            } else {
                ofGoogle(userNameAttributeName, attributes)
            }
        }

        fun ofNaver(responseKey: String, attributes: Map<String, Any>): OAuthAttributes {
            val response = attributes[responseKey] as Map<String, Any>
            return OAuthAttributes(
                attributes,
                responseKey,
                response["email"] as String,
                response["name"] as String,
                response["gender"] as String,
                response["birthYear"] as String
            )
        }

        fun ofGoogle(userNameAttributeName: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                attributes,
                userNameAttributeName,
                attributes["email"] as String,
                attributes["name"] as String,
                attributes["gender"] as String,
                attributes["birthYear"] as String
            )
        }
    }
}