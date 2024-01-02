package land.land_be.config.security.oauth2

import land.land_be.domain.JoinType
import land.land_be.domain.RoleType

data class OAuthAttributes(
    var attributes: Map<String, Any>,
    var attributeKey: String,
    var email: String,
    var name: String,
    var gender: String,
    var birthYear: String,
    var joinType: JoinType,
    var role: RoleType
) {
    companion object {
        fun of(registrationId: String, attributeKey: String, attributes: Map<String, Any>) : OAuthAttributes {
            when (registrationId) {
                "google" -> return ofGoogle(attributeKey, attributes)
                "naver" -> return ofNaver("id", attributes)
                else -> throw RuntimeException()
            }
        }

        private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>): OAuthAttributes {
            return OAuthAttributes(
                attributes,
                attributeKey,
                attributes["email"] as String,
                attributes["name"] as String,
                attributes["gender"] as String,
                attributes["birthYear"] as String,
                JoinType.GOOGLE,
                RoleType.MEMBER
            )
        }

        private fun ofNaver(attributeKey: String, attributes: Map<String, Any>): OAuthAttributes {
            val response = attributes["response"] as Map<String, Any>
            return OAuthAttributes(
                attributes,
                attributeKey,
                response["email"] as String,
                response["name"] as String,
                response["gender"] as String,
                response["birthyear"] as String,
                JoinType.NAVER,
                RoleType.MEMBER
            )
        }
    }
}