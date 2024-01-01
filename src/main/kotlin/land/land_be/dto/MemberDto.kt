package land.land_be.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

class MemberDto {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class MemberRequestDto(
        var email: String,
        var name: String,

    )
}