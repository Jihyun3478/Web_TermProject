package web.termproject.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.RoleType;
import web.termproject.repository.MemberRepository;
import web.termproject.security.service.JwtTokenProvider;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

        if (memberRepository.findByLoginId(signupRequestDTO.getLoginId()).isPresent()) {
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

    @Transactional
    public Member findByLoginId(String loginId) throws BadRequestException {
        return memberRepository.findByLoginId(loginId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
    }

    public Member processOAuthPostLogin(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String nickname = null;
        String email = null;

        if (attributes.containsKey("properties") && attributes.containsKey("kakao_account")) {
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            nickname = (String) properties.get("nickname");
            email = (String) kakaoAccount.get("email");
        }

        if (nickname == null || email == null) {
            throw new IllegalArgumentException("OAuth2User attributes are missing required fields");
        }

        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if (!byEmail.isPresent()) {
            Member member = Member.builder()
                    .name(nickname)
                    .email(email)
                    .loginId(email)
                    .loginPw(passwordEncoder.encode(email))
                    .role(RoleType.MEMBER)
                    .major("미정")
                    .phoneNum("미정")
                    .build();
            memberRepository.save(member);
            return member;
        }
        return byEmail.get();
    }
}