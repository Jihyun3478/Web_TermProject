package web.termproject.service;

import org.springframework.security.core.Authentication;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.entity.Member;

public interface MemberService {
    void createMember(SignupRequestDTO signupRequestDTO);
    boolean confirmId(String loginId);
    boolean confirmNickname(String nickname);
    JwtTokenDTO signIn(String username, String password);
    Member findByLoginId(String loginId);
    Member processOAuthPostLogin(Authentication authentication);
}
