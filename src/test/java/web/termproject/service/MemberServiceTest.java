package web.termproject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.Member;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void 회원가입() {
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
                .loginId("테스트1111")
                .loginPw("테스트1111")
                .name("이름")
                .stuNum("학번")
                .major("소속")
                .phoneNum("전화번호")
                .email("이메일")
                .gender("성별")
                .birthDate("생년월일")
                .build();

        MemberResponseDTO memberResponseDTO = memberService.createMember(signupRequestDTO);

        assertThat(memberResponseDTO.getLoginId()).isEqualTo("테스트1111");
        assertThat(memberResponseDTO.getName()).isEqualTo("이름");
        assertThat(memberResponseDTO.getStuNum()).isEqualTo("학번");
        assertThat(memberResponseDTO.getMajor()).isEqualTo("소속");
        assertThat(memberResponseDTO.getPhoneNum()).isEqualTo("전화번호");
        assertThat(memberResponseDTO.getEmail()).isEqualTo("이메일");
        assertThat(memberResponseDTO.getGender()).isEqualTo("성별");
        assertThat(memberResponseDTO.getBirthDate()).isEqualTo("생년월일");
    }

    @Test
    void 로그인() {
        JwtTokenDTO jwtToken = memberService.signIn("test1111", "test1111");
        assertThat(jwtToken).isNotNull();
    }

    @Test
    void OAuth_로그인_처리() {
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        Map<String, Object> attributes = Map.of(
                "properties", Map.of("nickname", "사용자1"),
                "kakao_account", Map.of("email", "test@kakao.com")
        );
        when(oAuth2User.getAttributes()).thenReturn(attributes);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Member member = memberService.processOAuthPostLogin(authentication);
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo("test@kakao.com");
    }
}
