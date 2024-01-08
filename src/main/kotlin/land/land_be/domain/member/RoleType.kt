package land.land_be.domain.member

enum class RoleType(
    val role: String
) {
    GUEST("게스트"),
    MEMBER("회원")
}