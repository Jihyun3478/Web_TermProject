package web.termproject.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import web.termproject.docs.RestDocsSupport;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.dto.response.ProfessorResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.service.ApplyClubService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends RestDocsSupport {

    private final ApplyClubService applyClubService = mock(ApplyClubService.class);

    @Override
    protected Object initController() {
        return new AdminController(applyClubService);
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
                .name("John Doe")
                .major("Computer Science")
                .stuNum("20210001")
                .phoneNum("010-1234-5678")
                .pName("Prof. Smith")
                .pMajor("Software Engineering")
                .pPhoneNum("010-9876-5432")
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

//    @WithMockUser(username = "관리자", roles = {"ADMIN"})
//    @DisplayName("동아리 승인")
//    @Test
//    void acceptClub() throws Exception {
//        ApplyClub applyClub = new ApplyClub();
//        applyClub.setId(1L);
//        Professor professor = new Professor();
//        applyClub.setProfessor(professor);
//        Member member = new Member();
//        applyClub.setMember(member);
//
//        ClubResponseDTO clubResponseDTO = ClubResponseDTO.builder()
//                .id(1L)
//                .introduce("Introduction text")
//                .history("History text")
//                .imageRoute("image/route.png")
//                .meetingTime("Weekly on Mondays")
//                .president("President Name")
//                .vicePresident("Vice President Name")
//                .generalAffairs("General Affairs Name")
//                .clubType(ClubType.CENTRAL)
//                .name("Coding Club")
//                .build();
//
//        ModelMapper modelMapper = mock(ModelMapper.class);
//        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(); // Mocked ProfessorResponseDTO
//        MemberResponseDTO masterMemberResponseDTO = new MemberResponseDTO();   // Mocked MemberResponseDTO
//
//        when(modelMapper.map(any(Professor.class), eq(ProfessorResponseDTO.class))).thenReturn(professorResponseDTO);
//        when(modelMapper.map(any(Member.class), eq(MemberResponseDTO.class))).thenReturn(masterMemberResponseDTO);
//
//        given(applyClubService.findById(eq(1L))).willReturn(applyClub);
//        given(applyClubService.createClub((ApplyClub) any(ApplyClub.class))).willReturn(clubResponseDTO);
//
//        mockMvc.perform(post("/admin/accept/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("accept-club",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("동아리 ID").optional(),
//                                fieldWithPath("data.introduce").type(JsonFieldType.STRING).description("동아리 소개").optional(),
//                                fieldWithPath("data.history").type(JsonFieldType.STRING).description("동아리 역사").optional(),
//                                fieldWithPath("data.imageRoute").type(JsonFieldType.STRING).description("이미지 경로").optional(),
//                                fieldWithPath("data.meetingTime").type(JsonFieldType.STRING).description("모임 시간").optional(),
//                                fieldWithPath("data.president").type(JsonFieldType.STRING).description("회장 이름").optional(),
//                                fieldWithPath("data.vicePresident").type(JsonFieldType.STRING).description("부회장 이름").optional(),
//                                fieldWithPath("data.generalAffairs").type(JsonFieldType.STRING).description("총무 이름").optional(),
//                                fieldWithPath("data.professor").type(JsonFieldType.OBJECT).description("지도 교수 정보").optional(),
//                                fieldWithPath("data.applyMember").type(JsonFieldType.OBJECT).description("신청 회원 정보").optional(),
//                                fieldWithPath("data.masterMember").type(JsonFieldType.OBJECT).description("주임 회원 정보").optional(),
//                                fieldWithPath("data.clubType").type(JsonFieldType.STRING).description("동아리 유형"),
//                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("동아리 이름")
//                        )));
//    }

    @WithMockUser(username = "관리자", roles = {"ADMIN"})
    @DisplayName("동아리 거절")
    @Test
    void refuseClub() throws Exception {
        ApplyClub applyClub = new ApplyClub();
        applyClub.setId(1L);

        ApplyClubResponseDTO applyClubResponseDTO = ApplyClubResponseDTO.builder()
                .applyClubStatus(ApplyClubStatus.REFUSE)
                .clubType(ClubType.CENTRAL)
                .clubName("Coding Club")
                .name("John Doe")
                .major("Computer Science")
                .stuNum("20210001")
                .phoneNum("010-1234-5678")
                .pName("Prof. Smith")
                .pMajor("Software Engineering")
                .pPhoneNum("010-9876-5432")
                .refuseReason("Insufficient funding")
                .build();

        given(applyClubService.findById(1L)).willReturn(applyClub);
        given(applyClubService.refuseClub(eq(1L), eq("Insufficient funding"))).willReturn(applyClubResponseDTO);

        mockMvc.perform(post("/admin/refuse/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refuseReason\":\"Insufficient funding\"}"))
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
                                fieldWithPath("data.refuseReason").type(JsonFieldType.STRING).description("거절 사유").optional(),
                                fieldWithPath("data.applyClubId").type(JsonFieldType.NUMBER).description("동아리 신청 ID").optional(),
                                fieldWithPath("data.applyClubStatus").type(JsonFieldType.STRING).description("동아리 신청 상태"),
                                fieldWithPath("data.clubType").type(JsonFieldType.STRING).description("동아리 유형"),
                                fieldWithPath("data.clubName").type(JsonFieldType.STRING).description("동아리 이름"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("신청자 이름"),
                                fieldWithPath("data.major").type(JsonFieldType.STRING).description("신청자 전공"),
                                fieldWithPath("data.stuNum").type(JsonFieldType.STRING).description("신청자 학번"),
                                fieldWithPath("data.phoneNum").type(JsonFieldType.STRING).description("신청자 전화번호"),
                                fieldWithPath("data.pname").type(JsonFieldType.STRING).description("지도 교수 이름"),
                                fieldWithPath("data.pmajor").type(JsonFieldType.STRING).description("지도 교수 전공"),
                                fieldWithPath("data.pphoneNum").type(JsonFieldType.STRING).description("지도 교수 전화번호")
                        )));
    }
}
