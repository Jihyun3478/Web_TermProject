package web.termproject.service;

import org.springframework.security.core.Authentication;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.Member;

import java.util.List;

public interface MemberService {
    MemberResponseDTO createMember(SignupRequestDTO signupRequestDTO);
    JwtTokenDTO signIn(String username, String password);
    Member findByLoginId(String loginId);
    Member processOAuthPostLogin(Authentication authentication);

    List<Long> findClubIdsByLoginId(String loginId);

    List<String> findClubNamesByLoginId(String loginId);

    Member findByName(String memberName);

    void save(Member member);
}
