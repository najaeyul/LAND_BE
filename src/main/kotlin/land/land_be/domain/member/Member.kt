package land.land_be.domain.member

import jakarta.persistence.*
import land.land_be.common.DateInfo

@Entity
class Member(

    val email: String,
    val name: String,
    val gender: String? = null,
    val birthYear: String? = null,

    @Enumerated(EnumType.STRING)
    val joinType: JoinType,
    @Enumerated(EnumType.STRING)
    val role: RoleType,

    @Id @Column(name = "member_id")
    val id: String

) : DateInfo()