package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.Member;
import web.termproject.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDTO createAdmin(SignupRequestDTO signupRequestDTO) {
        Member member = signupRequestDTO.toEntity();
        member.addAdminAuthority();
        member.encodePassword(passwordEncoder);

        Member savedMember = memberRepository.save(member);
        return MemberResponseDTO.builder()
                .loginId(savedMember.getLoginId())
                .loginPw(savedMember.getLoginPw())
                .name(savedMember.getName())
                .stuNum(savedMember.getStuNum())
                .major(savedMember.getMajor())
                .phoneNum(savedMember.getPhoneNum())
                .email(savedMember.getEmail())
                .gender(savedMember.getGender())
                .birthDate(savedMember.getBirthDate())
                .build();
    }
}
