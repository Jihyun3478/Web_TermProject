package web.termproject.docs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import web.termproject.controller.MemberController;
import web.termproject.domain.dto.request.JwtTokenDTO;
import web.termproject.domain.dto.request.LoginRequestDTO;
import web.termproject.domain.dto.request.SignupRequestDTO;
import web.termproject.domain.dto.response.MemberResponseDTO;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.RoleType;
import web.termproject.service.ApplyClubService;
import web.termproject.service.MemberService;
import web.termproject.service.ProfessorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerDocsTest extends RestDocsSupport {

    @MockBean
    private MemberService memberService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private ApplyClubService applyClubService;

    @Override
    protected Object initController() {
        return new MemberController(memberService, professorService, applyClubService);
    }

    @DisplayName("회원가입")
    @Test
    void signUp() throws Exception {
        SignupRequestDTO request = SignupRequestDTO.builder()
                .loginId("test1234")
                .loginPw("test1234")
                .name("홍길동")
                .stuNum("20240001")
                .major("컴퓨터소프트웨어공학과")
                .phoneNum("000-0000-0000")
                .email("test@kumoh.ac.kr")
                .gender("남")
                .birthDate("2000-01-01")
                .role(RoleType.MEMBER)
                .build();

        given(memberService.createMember(any(SignupRequestDTO.class)))
                .willReturn(MemberResponseDTO.builder()
                        .id(1L)
                        .loginId(request.getLoginId())
                        .loginPw(request.getLoginPw())
                        .name(request.getName())
                        .stuNum(request.getStuNum())
                        .major(request.getMajor())
                        .phoneNum(request.getPhoneNum())
                        .email(request.getEmail())
                        .gender(request.getGender())
                        .birthDate(request.getBirthDate())
                        .role(request.getRole())
                        .build());

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                fieldWithPath("loginPw").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("stuNum").type(JsonFieldType.STRING).description("학번"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("전공"),
                                fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("role").type(JsonFieldType.STRING).optional().description("사용자 권한").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("data.loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                fieldWithPath("data.loginPw").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("data.stuNum").type(JsonFieldType.STRING).description("학번"),
                                fieldWithPath("data.major").type(JsonFieldType.STRING).description("전공"),
                                fieldWithPath("data.phoneNum").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("data.gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("data.birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).optional().description("사용자 권한"),
                                fieldWithPath("data.grantType").type(JsonFieldType.STRING).optional().description("사용자 인증 유형")
                        )
                ));
    }

    @DisplayName("로그인")
    @Test
    void signIn() throws Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .loginId("test1234")
                .loginPw("test1234")
                .build();

        JwtTokenDTO jwtTokenDTO = JwtTokenDTO.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .grantType("Bearer")
                .build();

        given(memberService.signIn(any(String.class), any(String.class))).willReturn(jwtTokenDTO);

        mockMvc.perform(post("/api/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-signin",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                fieldWithPath("loginPw").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                fieldWithPath("grantType").type(JsonFieldType.STRING).optional().description("사용자 인증 유형")
                        )
                ));
    }

    @WithMockUser(username = "test1234", roles = {"MEMBER"})
    @DisplayName("회원_전체_정보_조회")
    @Test
    void memberDetailInfo() throws Exception {
        String loginId = "test1234";

        given(memberService.findByLoginId(loginId)).willReturn(null);

        mockMvc.perform(get("/api/memberInfo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        Member member = new Member();
        member.setId(1L);
        member.setLoginId(loginId);
        member.setLoginPw("test1234");
        member.setName("홍길동");
        member.setStuNum("2024");
        member.setMajor("컴퓨터소프트웨어공학과");
        member.setPhoneNum("000-0000-0000");
        member.setEmail("홍길동@kumoh.ac.kr");
        member.setGender("남");
        member.setBirthDate("2000-01-01");
        member.setRole(RoleType.MEMBER);

        given(memberService.findByLoginId(loginId)).willReturn(member);

        mockMvc.perform(get("/api/memberInfo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-detail-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                fieldWithPath("loginId").type(JsonFieldType.STRING).description("로그인 ID"),
                                fieldWithPath("loginPw").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("stuNum").type(JsonFieldType.STRING).description("학번"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("전공"),
                                fieldWithPath("phoneNum").type(JsonFieldType.STRING).description("전화번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING).description("생년월일"),
                                fieldWithPath("role").type(JsonFieldType.STRING).optional().description("사용자 권한"),
                                fieldWithPath("grantType").type(JsonFieldType.STRING).optional().description("사용자 인증 유형")
                        )
                ));
    }
}
