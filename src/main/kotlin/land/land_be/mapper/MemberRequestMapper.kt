package land.land_be.mapper

import land.land_be.domain.JoinType
import land.land_be.domain.RoleType
import land.land_be.dto.MemberDto
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class UserRequestMapper {
    fun toDto(oAuth2User: OAuth2User): MemberDto.MemberRequestDto {
        val attributes = oAuth2User.attributes
        return MemberDto.MemberRequestDto(
            email = attributes["email"] as String,
            name = attributes["name"] as String,
            gender = attributes["picture"] as String,
            birthYear = attributes["birthYear"] as String,
            joinType =  JoinType.GOOGLE,
            role = RoleType.MEMBER
        )
    }
}