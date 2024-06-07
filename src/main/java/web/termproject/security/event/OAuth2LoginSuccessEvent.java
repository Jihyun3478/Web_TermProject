package web.termproject.security.event;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.Authentication;

@Getter
public class OAuth2LoginSuccessEvent extends ApplicationEvent {

    private final Authentication authentication;
    private final HttpServletResponse response;

    public OAuth2LoginSuccessEvent(Authentication authentication, HttpServletResponse response) {
        super(authentication);
        this.authentication = authentication;
        this.response = response;
    }
}