package land.land_be.dto.member

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import land.land_be.domain.member.JoinType
import land.land_be.domain.member.RoleType

class MemberDto {

    data class TokenDto(
        val authToken: String,
        val refreshToken: String,
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class MemberRequestDto(
        val id: String,
        val email: String,
        val name: String,
        val gender: String,
        var birthYear: String,
        val joinType: JoinType,
        val role: RoleType
    )
}