package land.land_be.config.security.oauth2

import org.hibernate.query.sqm.tree.SqmNode.log
import org.slf4j.LoggerFactory


data class OAuthAttribute(
    var attributes: Map<String, Any>,
    var attributeKey: String,
    var email: String,
    var name: String,
    var gender: String,
    var birthYear: String,

) {
    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        fun of(registrationId: String, attributeKey: String, attributes: Map<String, Any>) : OAuthAttribute {
            when (registrationId) {
                "google" -> return ofGoogle(attributeKey, attributes)
                "naver" -> return ofNaver("id", attributes)
                else -> throw RuntimeException()
            }
        }

        private fun ofGoogle(attributeKey: String, attributes: Map<String, Any>): OAuthAttribute {
            var arr: () -> Unit = {attributes["email"]}
            arr.apply { attributes["name"] }
            arr.apply { attributes["gender"] }
            arr.apply { attributes["birthday"] }

            log.info(arr)
            return OAuthAttribute(
                attributes,
                attributeKey,
                attributes["email"] as? String ?: "",
                attributes["name"] as? String ?: "",
                attributes["gender"] as? String ?: "",
                attributes["birthday"] as? String ?: ""
            )
        }

        private fun ofNaver(attributeKey: String, attributes: Map<String, Any>): OAuthAttribute {
            val response = attributes["response"] as Map<String, Any>
            return OAuthAttribute(
                attributes,
                attributeKey,
                response["email"] as String,
                response["name"] as String,
                response["gender"] as String,
                response["birthyear"] as String
            )
        }
    }
    fun convertToMap(): Map<String, Any> {
        return mapOf(
            "id" to attributeKey,
            "key" to attributeKey,
            "name" to name,
            "email" to email,
            "gender" to gender,
            "birthYear" to birthYear
        )
    }
}