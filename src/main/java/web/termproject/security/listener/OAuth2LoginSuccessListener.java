package web.termproject.security.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.entity.Member;
import web.termproject.security.event.OAuth2LoginSuccessEvent;
import web.termproject.security.service.JwtTokenProvider;
import web.termproject.service.MemberService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessListener {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @EventListener
    public void handleOAuth2LoginSuccess(OAuth2LoginSuccessEvent event) throws IOException {
        Member member = memberService.processOAuthPostLogin(event.getAuthentication());
        JwtTokenDTO jwtToken = memberService.signIn(member.getEmail(), member.getEmail());
        event.getResponse().sendRedirect("http://localhost:3000/loginSuccess?token=" + jwtToken.getAccessToken());
    }
}