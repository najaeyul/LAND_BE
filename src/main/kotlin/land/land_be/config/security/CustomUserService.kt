package land.land_be.config.security

import land.land_be.repository.member.MemberRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserService(private val memberRepository: MemberRepository):
    UserDetailsService {
        override fun loadUserByUsername(username: String): UserDetails {
            val member = memberRepository.findById(username)
                .orElseThrow { UsernameNotFoundException("user is not found") }
            val authorities = listOf<GrantedAuthority>(SimpleGrantedAuthority(member.role.role))
            return CustomUser(member, authorities)
        }
}