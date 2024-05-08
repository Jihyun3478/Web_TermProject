package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.entity.Member;
import web.termproject.repository.MemberRepository;
import web.termproject.security.service.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void createMember(SignupRequestDTO signupRequestDTO) throws BadRequestException {
        Member member = signupRequestDTO.toEntity();

        member.addUserAuthority();
        member.encodePassword(passwordEncoder);

        if(memberRepository.findByLoginId(signupRequestDTO.getLoginId()).isPresent()){
            throw new BadRequestException("이미 존재하는 아이디입니다.");
        }
        memberRepository.save(member);
    }

    @Transactional
    public boolean confirmId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    @Transactional
    public boolean confirmNickname(String nickname) {
        return memberRepository.existsByName(nickname);
    }

    @Transactional
    public JwtTokenDTO signIn(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}
