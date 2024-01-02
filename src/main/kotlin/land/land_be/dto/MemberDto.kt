package land.land_be.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import land.land_be.domain.JoinType
import land.land_be.domain.RoleType

class MemberDto {

    data class TokenDto(
        val token: String,
        val refreshToken: String,
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class MemberRequestDto(
        val email: String,
        val name: String,
        val gender: String,
        var birthYear: String,
        val joinType: JoinType,
        val role: RoleType
    )
}