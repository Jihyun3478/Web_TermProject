package web.termproject.docs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import web.termproject.controller.ApplyMemberController;
import web.termproject.domain.dto.response.ApplyMemberReponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.dto.response.ProfessorResponseDTO;
import web.termproject.domain.status.ClubType;
import web.termproject.domain.status.RoleType;
import web.termproject.service.ApplyMemberService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ApplyMemberController.class)
class ApplyMemberControllerDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplyMemberService applyMemberService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @WithMockUser
    public void testDownloadFile() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.get("/applyMember/download"))
                .andExpect(status().isOk())
                .andDo(document("applyMember-download",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    @WithMockUser
    public void testUploadFiles() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/applyMember/upload/{clubId}", 1L)
                        .file("files", "test content".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(status().isOk())
                .andDo(document("applyMember-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("Basic auth credentials"))
                ));
    }

    @Test
    @WithMockUser
    public void testGetClubList() throws Exception {
        List<ApplyMemberReponseDTO> applyMemberReponseDTOS = null;
        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(1L)
                .major("컴퓨터소프트웨어공학과")
                .stuNum("12341234")
                .name("마스터 멤버 이름")
                .phoneNum("0000000000")
                .email("123@123.com")
                .role(RoleType.MASTER_MEMBER)
                .gender("남")
                .loginId("마스터멤버 id")
                .loginPw("마스터멤버 비밀번호")
                .birthDate("마스터멤버 생일")
                .build();

        ProfessorResponseDTO professorResponseDTO = ProfessorResponseDTO.builder()
                .id(1L)
                .email("345@345.com")
                .major("컴퓨터소프트웨어공학과")
                .name("테스트 교수님")
                .phoneNum("1231234123")
                .build();

        List <ClubResponseDTO> clubResponseDTOList = List.of(ClubResponseDTO.builder()
                .id(1L)
                .clubType(ClubType.CENTRAL)
                .name("테스트 클럽 이름")
                .history("테스트 클럽 역사")
                .imageRoute("테스트 클럽 이미지 경로")
                .applyMember(applyMemberReponseDTOS)
                .MasterMember(memberResponseDTO)
                .generalAffairs("회계사 명")
                .president("회장 명")
                .vicePresident("부회장 명")
                .meetingTime("정기회의 시간")
                .professor(professorResponseDTO)
                .build());

        given(applyMemberService.getNotApplyMemberClubList(any(String.class)))
                .willReturn(clubResponseDTOList);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/applyMember/clubList")
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(status().isOk())
                .andDo(document("applyMember-clubList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("Basic auth credentials"))
                ));
    }
}