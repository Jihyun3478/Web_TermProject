package web.termproject.controller;

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
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.service.ApplyMemberService;
import web.termproject.service.ImageService;
import web.termproject.service.MasterService;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/master/club/list")
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(status().isOk())
                .andDo(document("master-club-list", requestHeaders(
                        headerWithName("Authorization")
                                .description("Basic auth credentials")
                )));
    }

    @Test
    @WithMockUser
    public void testUpdateMasterClubInfo() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/master/club/{clubId}", 1L)
                        .header("Authorization", "Bearer your_token_here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"update_name\", \"introduce\":\"update_introduce\", \"imageRoute\": \"update_imageRoute\", \"meetingtime\": \"update_meetingTime\", \"president\": \"update_president\", \"vicePresident\": \"update_vicePresident\", \"generalAffairs\": \"update_generalAffairs\"}"))
                .andExpect(status().isOk())
                .andDo(document("master-club-update", requestHeaders(
                        headerWithName("Authorization")
                                .description("Basic auth credentials")
                )));
    }

    @Test
    @WithMockUser
    public void testUploadClubImage() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/master/club/uploadImage/{clubId}", 1L)
                        .file("image", "test content".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(document("master-club-uploadImage"));
    }

    @Test
    @WithMockUser
    public void testUpdateApplyMemberStatus() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/master/club/applyMember/{applyMemberId}", 1L)
                        .param("ApplyMemberStatus", ApplyMemberStatus.CLUB_MEMBER.toString()))
                .andExpect(status().isOk())
                .andDo(document("master-applyMember-updateStatus"));
    }

    @Test
    @WithMockUser
    public void testDownloadApplyForm() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/master/club/applyMember/{clubId}/{memberId}", 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("master-applyMember-downloadForm"));
    }
}