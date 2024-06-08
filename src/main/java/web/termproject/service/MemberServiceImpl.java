package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.entity.Member;
import web.termproject.repository.MemberRepository;
import web.termproject.security.service.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void createMember(SignupRequestDTO signupRequestDTO) {
        Member member = signupRequestDTO.toEntity();
        member.addUserAuthority();
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);
    }

    public boolean confirmId(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public boolean confirmNickname(String nickname) {
        return memberRepository.existsByName(nickname);
    }

    public JwtTokenDTO signIn(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

    public Member findByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return member;
    }
}
