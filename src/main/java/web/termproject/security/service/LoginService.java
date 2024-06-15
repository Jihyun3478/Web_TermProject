package web.termproject.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.entity.Member;
import web.termproject.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        if(memberRepository.findByLoginId(loginId) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Member member = memberRepository.findByLoginId(loginId);
        return User.builder()
                .username(member.getLoginId())
                .password(member.getLoginPw())
                .roles(member.getRole().name())
                .build();
    }
}
