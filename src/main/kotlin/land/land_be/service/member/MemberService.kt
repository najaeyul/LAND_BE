package land.land_be.service.member

import jakarta.transaction.Transactional
import land.land_be.domain.member.Member
import land.land_be.dto.member.MemberDto
import land.land_be.repository.member.MemberRepository
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    /**
     * 회원가입
     */
    fun signUp(memberRequestDto: MemberDto.MemberRequestDto) {
        val member = Member(
            id = memberRequestDto.id,
            email = memberRequestDto.email,
            name = memberRequestDto.name,
            gender = memberRequestDto.gender,
            birthYear = memberRequestDto.birthYear,
            joinType = memberRequestDto.joinType,
            role = memberRequestDto.role
        )

        memberRepository.save(member)
    }

    /**
     * id로 회원 존재여부 체크
     */
    fun checkIfUserExists(id: String): Boolean {
        return memberRepository.existsById(id)
    }
}