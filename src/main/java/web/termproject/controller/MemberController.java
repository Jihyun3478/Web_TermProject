package web.termproject.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.LoginRequestDTO;
import web.termproject.domain.dto.request.PSignupRequestDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.ApiResponse;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.RoleType;
import web.termproject.exception.ResponseCode;
import web.termproject.security.util.SecurityUtil;
import web.termproject.service.MemberService;
import web.termproject.service.ProfessorService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ProfessorService professorService;

    /* 회원가입 */
    @PostMapping("/api/signup")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequestDTO requestDTO) {
        MemberResponseDTO responseDTO = memberService.createMember(requestDTO);

        return ResponseEntity.ok(ApiResponse.response(ResponseCode.Created, "회원가입 완료", responseDTO));
    }

    @GetMapping("/confirmLoginId/{loginId}")
    public ResponseEntity<String> confirmId(@PathVariable("loginId") String loginId) {
        if(memberService.confirmId(loginId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok("사용 가능한 아이디 입니다.");
        }
    }

    @GetMapping("/confirmNickname/{nickname}")
    public ResponseEntity<String> confirmNickname(@PathVariable("nickname") String nickname) {
        if(memberService.confirmNickname(nickname)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임 입니다.");
        }
    }

    @PostMapping("/api/signin")
    public JwtTokenDTO signIn(@RequestBody LoginRequestDTO requestDTO) {
        String username = requestDTO.getLoginId();
        String password = requestDTO.getLoginPw();
        JwtTokenDTO jwtToken = memberService.signIn(username, password);

        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return jwtToken;
    }

    @GetMapping("/api/memberInfo")
    public ResponseEntity<MemberResponseDTO> getMemberInfo() {
        String loginId = SecurityUtil.getLoginId();
        Member findMember = memberService.findByLoginId(loginId);
        if (findMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(findMember.getId())
                .loginId(findMember.getLoginId())
                .loginPw(findMember.getLoginPw())
                .name(findMember.getName())
                .stuNum(findMember.getStuNum())
                .major(findMember.getMajor())
                .phoneNum(findMember.getPhoneNum())
                .email(findMember.getEmail())
                .gender(findMember.getGender())
                .birthDate(findMember.getBirthDate())
                .build();

        return ResponseEntity.ok(memberResponseDTO);
    }



    @PostConstruct
    public void initData() {
        SignupRequestDTO member1 = SignupRequestDTO.builder()
                .loginId("test1234")
                .loginPw("test1234")
                .name("홍길동")
                .stuNum("2024")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("000-0000-0000")
                .email("홍길동@kumoh.ac.kr")
                .gender("남")
                .birthDate("2000-01-01")
                .build();

        memberService.createMember(member1);

        SignupRequestDTO admin1 = SignupRequestDTO.builder()
                .loginId("admin1234")
                .loginPw("admin1234")
                .name("관리자")
                .stuNum("1111")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("111-1111-1111")
                .email("admin@kumoh.ac.kr")
                .gender("남")
                .birthDate("2000-01-01")
                .role(RoleType.ADMIN)
                .build();

        memberService.createMember(admin1);

        PSignupRequestDTO professor1 = PSignupRequestDTO.builder()
                .pLoginId("pTest1234")
                .loginPw("pTest1234")
                .name("교수님1")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("222-2222-2222")
                .email("교수님@kumoh.ac.kr")
                .build();

        professorService.createProfessor(professor1);
    }
}