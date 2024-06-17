package web.termproject.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.LoginRequestDTO;
import web.termproject.domain.dto.request.PSignupRequestDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.RoleType;
import web.termproject.exception.ApiResponse;
import web.termproject.exception.ResponseCode;
import web.termproject.security.util.SecurityUtil;
import web.termproject.service.AdminService;
import web.termproject.service.ApplyClubService;
import web.termproject.service.MemberService;
import web.termproject.service.ProfessorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final AdminService adminService;
    private final ProfessorService professorService;
    private final ApplyClubService applyClubService;

    /* 회원가입 */
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO requestDTO) {
        MemberResponseDTO responseDTO = memberService.createMember(requestDTO);

        return ResponseEntity.ok(ApiResponse.response(ResponseCode.Created, "회원가입 완료", responseDTO));
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

    @GetMapping("/member/applyClub/list")
    public ResponseEntity<?> applyClubList() {
        List<ApplyClubResponseDTO> responseDTOS = applyClubService.findApplyClubByMember(SecurityUtil.getLoginId());
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "동아리 신청 목록 조회", responseDTOS));
    }

   /* @PostConstruct
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

        adminService.createAdmin(admin1);

        PSignupRequestDTO professor1 = PSignupRequestDTO.builder()
                .pLoginId("pTest1234")
                .loginPw("pTest1234")
                .name("교수님1")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("222-2222-2222")
                .email("교수님@kumoh.ac.kr")
                .build();

        professorService.createProfessor(professor1);
*/
//        ApplyClubRequestDTO applyClub = ApplyClubRequestDTO.builder()
//                .clubType(ClubType.CENTRAL)
//                .clubName("동아리 신청 테스트")
//                .name("홍길동")
//                .major("컴퓨터소프트웨어공학과")
//                .stuNum("2024")
//                .phoneNum("000-0000-0000")
//                .pName("교수님1")
//                .pMajor("컴퓨터소프트웨어공학과")
//                .pPhoneNum("222-2222-2222")
//                .build();
//        applyClubService.createApplyClub(applyClub);
    //}
}