package web.termproject.docs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.controller.ImageController;
import web.termproject.service.ImageService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(ImageController.class)
class ImageControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

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
    public void testGetImage() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/image/display")
                        .param("imagePath", "test.jpg")
                        .accept(MediaType.IMAGE_JPEG))
                .andExpect(status().isOk())
                .andDo(document("image-display",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @WithMockUser
    public void testUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        Map<String, String> response = new HashMap<>();
        response.put("url", "test.jpg");

        when(imageService.uploadImage(any(MultipartFile.class), anyString())).thenReturn(response);

        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/image/upload")
                        .file(file)
                        .param("imagePath", "test.jpg")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("image-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}