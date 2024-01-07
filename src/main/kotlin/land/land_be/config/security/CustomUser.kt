package land.land_be.config.security

import land.land_be.domain.JoinType
import land.land_be.domain.Member
import land.land_be.domain.RoleType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
    private val member: Member,
    authorities: Collection<GrantedAuthority>
) : User(member.email, "", true, true, true, true, authorities) {
    val id: String = member.id
    val email: String = member.email
    val name:  String = member.name
    val gender: String? = member.gender
    val birthYear: String? = member.birthYear
    val joinType: JoinType = member.joinType
    val role: RoleType = member.role
}