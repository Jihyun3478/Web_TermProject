package web.termproject.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import web.termproject.controller.AdminController;
import web.termproject.domain.dto.request.RefuseApplyClubDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.dto.response.ProfessorResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.domain.status.RoleType;
import web.termproject.service.ApplyClubService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerDocsTest extends RestDocsSupport {

    private final ApplyClubService applyClubService = mock(ApplyClubService.class);

    private Member testMember;
    private Professor testProfessor;
    private ApplyClub testApplyClub;

    @Override
    protected Object initController() {
        return new AdminController(applyClubService);
    }

    @BeforeEach
    void initData() {
        testMember = Member.builder()
                .id(1L)
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

        testProfessor = Professor.builder()
                .id(1L)
                .loginId("pTest1234")
                .loginPw("pTest1234")
                .name("교수님1")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("222-2222-2222")
                .email("교수님@kumoh.ac.kr")
                .build();

        testApplyClub = ApplyClub.builder()
                .id(1L)
                .clubName("Coding Club")
                .applyClubStatus(ApplyClubStatus.WAIT)
                .clubType(ClubType.CENTRAL)
                .member(testMember)
                .professor(testProfessor)
                .build();
    }

    @WithMockUser(username = "관리자", roles = {"ADMIN"})
    @DisplayName("동아리 신청목록 조회")
    @Test
    void applyClubList() throws Exception {
        List<ApplyClubResponseDTO> applyClubList = new ArrayList<>();
        applyClubList.add(ApplyClubResponseDTO.builder()
                .applyClubStatus(ApplyClubStatus.WAIT)
                .clubType(ClubType.CENTRAL)
                .clubName("Coding Club")
                .name(testMember.getName())
                .major(testMember.getMajor())
                .stuNum(testMember.getStuNum())
                .phoneNum(testMember.getPhoneNum())
                .pName(testProfessor.getName())
                .pMajor(testProfessor.getMajor())
                .pPhoneNum(testProfessor.getPhoneNum())
                .build());

        given(applyClubService.findAll()).willReturn(applyClubList);

        mockMvc.perform(get("/admin/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("apply-club-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data[].applyClubId").type(JsonFieldType.NUMBER).description("동아리 신청 ID").optional(),
                                fieldWithPath("data[].refuseReason").type(JsonFieldType.STRING).description("거절 사유").optional(),
                                fieldWithPath("data[].applyClubStatus").type(JsonFieldType.STRING).description("동아리 신청 상태"),
                                fieldWithPath("data[].clubType").type(JsonFieldType.STRING).description("동아리 유형"),
                                fieldWithPath("data[].clubName").type(JsonFieldType.STRING).description("동아리 이름"),
                                fieldWithPath("data[].name").type(JsonFieldType.STRING).description("신청자 이름"),
                                fieldWithPath("data[].major").type(JsonFieldType.STRING).description("신청자 전공"),
                                fieldWithPath("data[].stuNum").type(JsonFieldType.STRING).description("신청자 학번"),
                                fieldWithPath("data[].phoneNum").type(JsonFieldType.STRING).description("신청자 전화번호"),
                                fieldWithPath("data[].pname").type(JsonFieldType.STRING).description("지도 교수 이름"),
                                fieldWithPath("data[].pmajor").type(JsonFieldType.STRING).description("지도 교수 전공"),
                                fieldWithPath("data[].pphoneNum").type(JsonFieldType.STRING).description("지도 교수 전화번호")
                        )));
    }

    @WithMockUser(username = "관리자", roles = {"ADMIN"})
    @DisplayName("동아리 신청 상세 조회")
    @Test
    void applyClubDetail() throws Exception {
        given(applyClubService.findById(1L)).willReturn(testApplyClub);

        mockMvc.perform(get("/admin/list/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("apply-club-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.applyClubId").type(JsonFieldType.NUMBER).description("동아리 신청 ID"),
                                fieldWithPath("data.applyClubStatus").type(JsonFieldType.STRING).description("동아리 신청 상태"),
                                fieldWithPath("data.clubType").type(JsonFieldType.STRING).description("동아리 유형"),
                                fieldWithPath("data.clubName").type(JsonFieldType.STRING).description("동아리 이름"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("신청자 이름"),
                                fieldWithPath("data.major").type(JsonFieldType.STRING).description("신청자 전공"),
                                fieldWithPath("data.stuNum").type(JsonFieldType.STRING).description("신청자 학번"),
                                fieldWithPath("data.phoneNum").type(JsonFieldType.STRING).description("신청자 전화번호"),
                                fieldWithPath("data.pname").type(JsonFieldType.STRING).description("지도 교수 이름"),
                                fieldWithPath("data.pmajor").type(JsonFieldType.STRING).description("지도 교수 전공"),
                                fieldWithPath("data.pphoneNum").type(JsonFieldType.STRING).description("지도 교수 전화번호"),
                                fieldWithPath("data.refuseReason").type(JsonFieldType.STRING).description("거절 사유").optional()
                        )));
    }

    @WithMockUser(username = "관리자", roles = {"ADMIN"})
    @DisplayName("동아리 승인")
    @Test
    void acceptClub() throws Exception {
        ClubResponseDTO responseDTO = ClubResponseDTO.builder()
                .id(1L)
                .clubType(testApplyClub.getClubType())
                .name(testApplyClub.getClubName())
                .professor(ProfessorResponseDTO.builder()
                        .id(testProfessor.getId())
                        .name(testProfessor.getName())
                        .major(testProfessor.getMajor())
                        .phoneNum(testProfessor.getPhoneNum())
                        .email(testProfessor.getEmail())
                        .build())
                .MasterMember(MemberResponseDTO.builder()
                        .id(testMember.getId())
                        .loginId(testMember.getLoginId())
                        .loginPw(testMember.getLoginPw())
                        .name(testMember.getName())
                        .stuNum(testMember.getStuNum())
                        .major(testMember.getMajor())
                        .phoneNum(testMember.getPhoneNum())
                        .email(testMember.getEmail())
                        .gender(testMember.getGender())
                        .birthDate(testMember.getBirthDate())
                        .role(RoleType.MASTER_MEMBER)
                        .build())
                .build();

        given(applyClubService.findById(1L)).willReturn(testApplyClub);
        given(applyClubService.createClub(testApplyClub)).willReturn(responseDTO);

        mockMvc.perform(post("/admin/accept/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accept-club",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("동아리 ID"),
                                fieldWithPath("data.clubType").type(JsonFieldType.STRING).description("동아리 유형").optional(),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("동아리 이름"),
                                fieldWithPath("data.professor.id").type(JsonFieldType.NUMBER).description("지도 교수 ID"),
                                fieldWithPath("data.professor.name").type(JsonFieldType.STRING).description("지도 교수 이름"),
                                fieldWithPath("data.professor.major").type(JsonFieldType.STRING).description("지도 교수 전공"),
                                fieldWithPath("data.professor.phoneNum").type(JsonFieldType.STRING).description("지도 교수 전화번호"),
                                fieldWithPath("data.professor.email").type(JsonFieldType.STRING).description("지도 교수 이메일"),
                                fieldWithPath("data.masterMember.id").type(JsonFieldType.NUMBER).description("마스터 회원 ID"),
                                fieldWithPath("data.masterMember.loginId").type(JsonFieldType.STRING).description("마스터 회원 로그인 ID"),
                                fieldWithPath("data.masterMember.loginPw").type(JsonFieldType.STRING).description("마스터 회원 로그인 비밀번호"),
                                fieldWithPath("data.masterMember.name").type(JsonFieldType.STRING).description("마스터 회원 이름"),
                                fieldWithPath("data.masterMember.stuNum").type(JsonFieldType.STRING).description("마스터 회원 학번"),
                                fieldWithPath("data.masterMember.major").type(JsonFieldType.STRING).description("마스터 회원 전공"),
                                fieldWithPath("data.masterMember.phoneNum").type(JsonFieldType.STRING).description("마스터 회원 전화번호"),
                                fieldWithPath("data.masterMember.email").type(JsonFieldType.STRING).description("마스터 회원 이메일"),
                                fieldWithPath("data.masterMember.gender").type(JsonFieldType.STRING).description("마스터 회원 성별"),
                                fieldWithPath("data.masterMember.birthDate").type(JsonFieldType.STRING).description("마스터 회원 생년월일"),
                                fieldWithPath("data.masterMember.role").type(JsonFieldType.STRING).description("마스터 회원 권한"),
                                fieldWithPath("data.introduce").type(JsonFieldType.STRING).description("동아리 소개").optional(),
                                fieldWithPath("data.history").type(JsonFieldType.STRING).description("동아리 역사").optional(),
                                fieldWithPath("data.imageRoute").type(JsonFieldType.STRING).description("동아리 이미지 경로").optional(),
                                fieldWithPath("data.meetingTime").type(JsonFieldType.STRING).description("동아리 모임 시간").optional(),
                                fieldWithPath("data.president").type(JsonFieldType.STRING).description("동아리 회장").optional(),
                                fieldWithPath("data.vicePresident").type(JsonFieldType.STRING).description("동아리 부회장").optional(),
                                fieldWithPath("data.generalAffairs").type(JsonFieldType.STRING).description("동아리 총무").optional(),
                                fieldWithPath("data.applyMember").type(JsonFieldType.ARRAY).description("신청자").optional()
                        )));
    }

    @WithMockUser(username = "관리자", roles = {"ADMIN"})
    @DisplayName("동아리 거절")
    @Test
    void refuseClub() throws Exception {
        RefuseApplyClubDTO refuseDTO = new RefuseApplyClubDTO("Reason for refusal");

        ApplyClubResponseDTO responseDTO = ApplyClubResponseDTO.builder()
                .applyClubId(testApplyClub.getId())
                .refuseReason(refuseDTO.getRefuseReason())
                .applyClubStatus(ApplyClubStatus.REFUSE)
                .clubType(testApplyClub.getClubType())
                .clubName(testApplyClub.getClubName())
                .name(testMember.getName())
                .major(testMember.getMajor())
                .stuNum(testMember.getStuNum())
                .phoneNum(testMember.getPhoneNum())
                .pName(testProfessor.getName())
                .pMajor(testProfessor.getMajor())
                .pPhoneNum(testProfessor.getPhoneNum())
                .build();

        given(applyClubService.refuseClub(eq(1L), anyString())).willReturn(responseDTO);

        mockMvc.perform(post("/admin/refuse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(refuseDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("refuse-club",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refuseReason").type(JsonFieldType.STRING).description("거절 사유")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data.applyClubId").type(JsonFieldType.NUMBER).description("동아리 신청 ID"),
                                fieldWithPath("data.refuseReason").type(JsonFieldType.STRING).description("거절 사유"),
                                fieldWithPath("data.applyClubStatus").type(JsonFieldType.STRING).description("동아리 신청 상태"),
                                fieldWithPath("data.clubType").type(JsonFieldType.STRING).description("동아리 유형"),
                                fieldWithPath("data.clubName").type(JsonFieldType.STRING).description("동아리 이름"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.major").type(JsonFieldType.STRING).description("전공"),
                                fieldWithPath("data.stuNum").type(JsonFieldType.STRING).description("학번"),
                                fieldWithPath("data.phoneNum").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("data.pname").type(JsonFieldType.STRING).description("교수 이름"),
                                fieldWithPath("data.pmajor").type(JsonFieldType.STRING).description("교수 전공"),
                                fieldWithPath("data.pphoneNum").type(JsonFieldType.STRING).description("교수 전화번호")
                        )));
    }
}