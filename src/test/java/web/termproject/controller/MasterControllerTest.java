package web.termproject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import web.termproject.domain.dto.response.ApplyMemberReponseDTO;
import web.termproject.domain.dto.response.ClubResponseDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.dto.response.ProfessorResponseDTO;
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.domain.status.ClubType;
import web.termproject.domain.status.RoleType;
import web.termproject.service.ApplyMemberService;
import web.termproject.service.ImageService;
import web.termproject.service.MasterService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(MasterController.class)
class MasterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MasterService masterService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private ApplyMemberService applyMemberService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @WithMockUser
    public void testGetClubs() throws Exception {
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

        List<ApplyMemberReponseDTO> applyMemberReponseDTOS = List.of(ApplyMemberReponseDTO.builder()
                        .applyMemberStatus(ApplyMemberStatus.CLUB_MEMBER)
                        .id(1L)
                        .member(memberResponseDTO)
                        .build());

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

        given(masterService.getMasterClubsInfo(any(String.class)))
                .willReturn(clubResponseDTOList);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/master/club/list")
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(status().isOk())
                .andDo(document("master-club-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                .description("Basic auth credentials"))
                ));
    }

    @Test
    @WithMockUser
    public void testUpdateMasterClubInfo() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.put("/master/club/{clubId}", 1L)
                        .header("Authorization", "Bearer your_token_here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"update_name\", \"introduce\":\"update_introduce\", \"imageRoute\": \"update_imageRoute\", \"meetingtime\": \"update_meetingTime\", \"president\": \"update_president\", \"vicePresident\": \"update_vicePresident\", \"generalAffairs\": \"update_generalAffairs\"}"))
                .andExpect(status().isOk())
                .andDo(document("master-club-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization")
                                        .description("Basic auth credentials"))
                        ));
    }

    @Test
    @WithMockUser
    public void testUploadClubImage() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/master/club/uploadImage/{clubId}", 1L)
                        .file("image", "test content".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(document("master-club-uploadImage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        ));
    }

    @Test
    @WithMockUser
    public void testUpdateApplyMemberStatus() throws Exception {
        given(masterService.updateApplyMemberStatus(any(Long.class), any(ApplyMemberStatus.class)))
                .willReturn(ResponseEntity.ok("updateApplyMemberStatus"));
        mockMvc.perform(RestDocumentationRequestBuilders.post("/master/club/applyMember/{applyMemberId}", 1L)
                        .param("ApplyMemberStatus", ApplyMemberStatus.CLUB_MEMBER.toString()))
                .andExpect(status().isOk())
                .andDo(document("master-applyMember-updateStatus",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @WithMockUser
    public void testDownloadApplyForm() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/master/club/applyMember/{clubId}/{memberId}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("master-applyMember-downloadForm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}