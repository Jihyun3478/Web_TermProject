package web.termproject.docs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import web.termproject.controller.ApplyClubController;
import web.termproject.docs.RestDocsSupport;
import web.termproject.domain.dto.request.ApplyClubRequestDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.Member;
import web.termproject.domain.entity.Professor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.service.ApplyClubService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApplyClubControllerDocsTest extends RestDocsSupport {

    private final ApplyClubService applyClubService = mock(ApplyClubService.class);
    private Member testMember;
    private Professor testProfessor;

    @Override
    protected Object initController() {
        return new ApplyClubController(applyClubService);
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
    }

    @DisplayName("동아리 등록 신청")
    @Test
    void test() throws Exception {
        ApplyClubRequestDTO requestDTO = ApplyClubRequestDTO.builder()
                .clubType(ClubType.CENTRAL)
                .clubName("Basketball Club")
                .name(testMember.getName())
                .major(testMember.getMajor())
                .stuNum(testMember.getStuNum())
                .phoneNum(testMember.getPhoneNum())
                .pName(testProfessor.getName())
                .pMajor(testProfessor.getMajor())
                .pPhoneNum(testProfessor.getPhoneNum())
                .build();

        given(applyClubService.createApplyClub(any(ApplyClubRequestDTO.class)))
                .willReturn(ApplyClubResponseDTO.builder()
                        .applyClubId(2L)
                        .applyClubStatus(ApplyClubStatus.WAIT)
                        .clubType(requestDTO.getClubType())
                        .clubName(requestDTO.getClubName())
                        .name("이지현")
                        .major("컴퓨터소프트웨어공학과")
                        .stuNum("홍길동")
                        .phoneNum("000-0000-0000")
                        .pName("교수님1")
                        .pMajor("컴퓨터소프트웨어공학과")
                        .pPhoneNum("222-2222-2222")
                        .build());

        mockMvc.perform(post("/applyClub/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("applyClub-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("clubType").type(JsonFieldType.STRING)
                                        .description("동아리 종류"),
                                fieldWithPath("clubName").type(JsonFieldType.STRING)
                                        .description("동아리 이름"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("신청자 이름"),
                                fieldWithPath(".major").type(JsonFieldType.STRING).description("신청자 전공"),
                                fieldWithPath("stuNum").type(JsonFieldType.STRING).description("신청자 학번"),
                                fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("신청자 전화번호"),
                                fieldWithPath("pname").type(JsonFieldType.STRING).description("지도 교수 이름"),
                                fieldWithPath("pmajor").type(JsonFieldType.STRING).description("지도 교수 전공"),
                                fieldWithPath("pphoneNum").type(JsonFieldType.STRING).description("지도 교수 전화번호")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답데이터"),
                                fieldWithPath("data.applyClubId").type(JsonFieldType.NUMBER)
                                        .description("동아리 신청 식별자"),
                                fieldWithPath("data.applyClubStatus").type(JsonFieldType.STRING)
                                        .description("동아리 신청 현황"),
                                fieldWithPath("data.clubType").type(JsonFieldType.STRING)
                                        .description("동아리 종류"),
                                fieldWithPath("data.clubName").type(JsonFieldType.STRING)
                                        .description("동아리 이름"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING)
                                        .description("신청자 이름"),
                                fieldWithPath("data.major").type(JsonFieldType.STRING)
                                        .description("신청자 소속"),
                                fieldWithPath("data.stuNum").type(JsonFieldType.STRING)
                                        .description("신청자 학번"),
                                fieldWithPath("data.phoneNum").type(JsonFieldType.STRING)
                                        .description("신청자 전화번호"),
                                fieldWithPath("data.pname").type(JsonFieldType.STRING)
                                        .description("교수님 성함"),
                                fieldWithPath("data.pmajor").type(JsonFieldType.STRING)
                                        .description("교수님 소속"),
                                fieldWithPath("data.pphoneNum").type(JsonFieldType.STRING)
                                        .description("교수님 전화번호"),
                                fieldWithPath("data.refuseReason").type(JsonFieldType.STRING)
                                        .description("거절 사유").optional()
                        )
                ));
    }
}