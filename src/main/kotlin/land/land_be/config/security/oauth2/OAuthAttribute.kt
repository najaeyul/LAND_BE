package land.land_be.config.security.oauth2


data class OAuthAttribute(
    var attributes: Map<String, Any>,
    var attributeKey: String,
    var id: String,
    var email: String,
    var name: String,
    var gender: String,
    var birthYear: String,
) {

    /**
     * 서비스별 데이터 객체 받기
     */
    companion object {
        fun of(registrationId: String, attributeKey: String, attributes: Map<String, Any>) : OAuthAttribute {
            when (registrationId) {
                "google" -> return ofGoogle(attributeKey, attributes)
                "naver" -> return ofNaver("id", attributes)
                else -> throw RuntimeException()
            }
        }

        private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>): OAuthAttribute {
            return OAuthAttribute(
                attributes,
                attributeKey,
                attributes["sub"] as? String ?: "",
                attributes["email"] as? String ?: "",
                attributes["name"] as? String ?: "",
                attributes["gender"] as? String ?: "",
                attributes["birthday"] as? String ?: ""
            )
        }

        private fun ofNaver(attributeKey: String, attributes: Map<String, Any>): OAuthAttribute {
            val response = attributes["response"] as Map<String, Any>
            val gender = when (attributes["gender"] as? String) {
                "male" -> "M"
                "female" -> "F"
                else -> ""
            }
            val birthday = (attributes["birthday"] as? String)?.substring(0, 4) ?: ""

            return OAuthAttribute(
                attributes,
                attributeKey,
                response["id"] as? String ?: "",
                response["email"] as String,
                response["name"] as String,
                gender,
                birthday
            )
        }
    }
    fun convertToMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "key" to attributeKey,
            "name" to name,
            "email" to email,
            "gender" to gender,
            "birthYear" to birthYear
        )
    }
}