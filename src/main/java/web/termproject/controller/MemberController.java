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
import web.termproject.domain.dto.request.*;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.domain.status.RoleType;
import web.termproject.exception.ApiResponse;
import web.termproject.exception.ResponseCode;
import web.termproject.repository.MemberRepository;
import web.termproject.repository.ProfessorRepository;
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
                .role(findMember.getRole())
                .build();

        return ResponseEntity.ok(memberResponseDTO);
    }

    @GetMapping("/member/applyClub/list")
    public ResponseEntity<?> applyClubList() {
        List<ApplyClubResponseDTO> responseDTOS = applyClubService.findApplyClubByMember(SecurityUtil.getLoginId());
        return ResponseEntity.ok(ApiResponse.response(ResponseCode.OK, "동아리 신청 목록 조회", responseDTOS));
    }

    @PostConstruct
    public void initData() {
        initAdmins();
        initMembers();
        initProfessors();
        initApplyClubs();
        initClubs();
    }

    private void initAdmins() {
        createAdmin("admin0000", "admin0000", "관리자", "0000", "컴퓨터소프트웨어공학과", "000-0000-0000", "admin@kumoh.ac.kr", "남", "2000-01-01");
    }

    private void createAdmin(String loginId, String loginPw, String name, String stuNum, String major, String phoneNum, String email, String gender, String birthDate) {
        SignupRequestDTO admin = SignupRequestDTO.builder()
                .loginId(loginId)
                .loginPw(loginPw)
                .name(name)
                .stuNum(stuNum)
                .major(major)
                .phoneNum(phoneNum)
                .email(email)
                .gender(gender)
                .birthDate(birthDate)
                .role(RoleType.ADMIN)
                .build();
        adminService.createAdmin(admin);
    }

    private void initMembers() {
        createMember("test1111", "test1111", "사용자1", "1111", "컴퓨터소프트웨어공학과", "111-1111-1111", "사용자1@kumoh.ac.kr", "남", "2001-01-01");
        createMember("test2222", "test2222", "사용자2", "2222", "컴퓨터소프트웨어공학과", "222-2222-2222", "사용자2@kumoh.ac.kr", "여", "2001-02-02");
        createMember("test3333", "test3333", "사용자3", "3333", "기계공학과", "333-3333-3333", "사용자3@kumoh.ac.kr", "남", "2001-03-03");
        createMember("test4444", "test4444", "사용자4", "4444", "기계공학과", "444-4444-4444", "사용자4@kumoh.ac.kr", "여", "2001-04-04");
        createMember("test5555", "test5555", "사용자5", "5555", "전자공학부", "555-5555-5555", "사용자5@kumoh.ac.kr", "남", "2001-05-05");
        createMember("test6666", "test6666", "사용자6", "6666", "전자공학부", "666-6666-6666", "사용자6@kumoh.ac.kr", "여", "2001-06-06");
        createMember("test7777", "test7777", "사용자7", "7777", "컴퓨터소프트웨어공학과", "777-7777-7777", "사용자7@kumoh.ac.kr", "여", "2001-07-07");
    }

    private void createMember(String loginId, String loginPw, String name, String stuNum, String major, String phoneNum, String email, String gender, String birthDate) {
        SignupRequestDTO member = SignupRequestDTO.builder()
                .loginId(loginId)
                .loginPw(loginPw)
                .name(name)
                .stuNum(stuNum)
                .major(major)
                .phoneNum(phoneNum)
                .email(email)
                .gender(gender)
                .birthDate(birthDate)
                .build();
        memberService.createMember(member);
    }

    private void initProfessors() {
        createProfessor("ptest1111", "ptest1111", "교수님1", "컴퓨터소프트웨어공학과", "111-1111-1111", "교수님1@kumoh.ac.kr");
        createProfessor("ptest2222", "ptest2222", "교수님2", "컴퓨터소프트웨어공학과", "222-2222-2222", "교수님2@kumoh.ac.kr");
        createProfessor("ptest3333", "ptest3333", "교수님3", "기계공학과", "333-3333-3333", "교수님3@kumoh.ac.kr");
        createProfessor("ptest4444", "ptest4444", "교수님4", "기계공학과", "444-4444-4444", "교수님4@kumoh.ac.kr");
        createProfessor("ptest5555", "ptest5555", "교수님5", "전자공학부", "555-5555-5555", "교수님5@kumoh.ac.kr");
        createProfessor("ptest6666", "ptest6666", "교수님6", "전자공학부", "666-6666-6666", "교수님6@kumoh.ac.kr");
        createProfessor("ptest7777", "ptest7777", "김성렬", "컴퓨터소프트웨어공학과", "000-0000-0000", "교수님7@kumoh.ac.kr");
    }

    private void createProfessor(String pLoginId, String loginPw, String name, String major, String phoneNum, String email) {
        PSignupRequestDTO professor = PSignupRequestDTO.builder()
                .pLoginId(pLoginId)
                .loginPw(loginPw)
                .name(name)
                .major(major)
                .phoneNum(phoneNum)
                .email(email)
                .build();
        professorService.createProfessor(professor);
    }

    private void initApplyClubs() {
        createApplyClub(ClubType.CENTRAL, "동아리1", ApplyClubStatus.WAIT, "사용자1", "교수님1");
        createApplyClub(ClubType.CENTRAL, "동아리2", ApplyClubStatus.WAIT, "사용자2", "교수님2");
        createApplyClub(ClubType.CENTRAL, "동아리3", ApplyClubStatus.REFUSE, "사용자3", "교수님3");
        createApplyClub(ClubType.DEPARTMENT, "동아리4", ApplyClubStatus.ACCEPT, "사용자4", "교수님4");
        createApplyClub(ClubType.DEPARTMENT, "동아리5", ApplyClubStatus.ACCEPT, "사용자5", "교수님5");
        createApplyClub(ClubType.DEPARTMENT, "동아리6", ApplyClubStatus.ACCEPT, "사용자6", "교수님6");
    }

    private void createApplyClub(ClubType clubType, String clubName, ApplyClubStatus status, String memberName, String professorName) {
        Member member = memberService.findByName(memberName);
        Professor professor = professorService.findByName(professorName);

        if(status == ApplyClubStatus.REFUSE) {
            ApplyClubRequestDTO applyClubRequestDTO = ApplyClubRequestDTO.builder()
                    .clubType(clubType)
                    .clubName(clubName)
                    .name(member.getName())
                    .major(member.getMajor())
                    .stuNum(member.getStuNum())
                    .phoneNum(member.getPhoneNum())
                    .pName(professor.getName())
                    .pMajor(professor.getMajor())
                    .pPhoneNum(professor.getPhoneNum())
                    .build();
            ApplyClub applyClub = applyClubService.createApplyClubEntity(applyClubRequestDTO);
            applyClub.setApplyClubStatus(status);
            applyClub.setRefuseReason("동일한 이름의 동아리가 존재합니다.");
            applyClubService.save(applyClub);
        }
        else {
            ApplyClubRequestDTO applyClubRequestDTO = ApplyClubRequestDTO.builder()
                    .clubType(clubType)
                    .clubName(clubName)
                    .name(member.getName())
                    .major(member.getMajor())
                    .stuNum(member.getStuNum())
                    .phoneNum(member.getPhoneNum())
                    .pName(professor.getName())
                    .pMajor(professor.getMajor())
                    .pPhoneNum(professor.getPhoneNum())
                    .build();

            ApplyClub applyClub = applyClubService.createApplyClubEntity(applyClubRequestDTO);
            applyClub.setApplyClubStatus(status);
            applyClubService.save(applyClub);
        }
    }

    private void initClubs() {
        List<ApplyClub> acceptedApplyClubs = applyClubService.findByApplyClubStatus(ApplyClubStatus.ACCEPT);
        for (ApplyClub acceptedApplyClub : acceptedApplyClubs) {
            applyClubService.createClub(acceptedApplyClub);
        }
    }
}