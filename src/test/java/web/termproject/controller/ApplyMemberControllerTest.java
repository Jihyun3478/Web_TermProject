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
import web.termproject.service.ApplyMemberService;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ApplyMemberController.class)
class ApplyMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    public void testDownloadFile() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/applyMember/download"))
                .andExpect(status().isOk())
                .andDo(document("applyMember-download"));
    }

    @Test
    @WithMockUser
    public void testUploadFiles() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/applyMember/upload/{clubId}", 1L)
                        .file("files", "test content".getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(status().isOk())
                .andDo(document("applyMember-upload", requestHeaders(
                        headerWithName("Authorization")
                                .description("Basic auth credentials"))
                ));
    }

    @Test
    @WithMockUser
    public void testGetClubList() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/applyMember/clubList")
                        .header("Authorization", "Bearer your_token_here"))
                .andExpect(status().isOk())
                .andDo(document("applyMember-clubList", requestHeaders(
                        headerWithName("Authorization")
                                .description("Basic auth credentials"))
                ));
    }
}