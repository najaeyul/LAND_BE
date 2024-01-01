package land.land_be.domain

import jakarta.persistence.*
import land.land_be.common.DateInfo

@Entity
class Member(

    val name: String,
    val gender: String? = null,
    val birthYear: String? = null,

    @Enumerated(EnumType.STRING)
    val joinType: JoinType,
    @Enumerated(EnumType.STRING)
    val role: RoleType,

    @Id
    @GeneratedValue
    val email: String
) : DateInfo() {
}