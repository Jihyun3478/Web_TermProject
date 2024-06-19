package web.termproject.docs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import web.termproject.controller.BoardController;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.status.BoardType;
import web.termproject.service.BoardService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardControllerDocsTest extends RestDocsSupport {

    private  final BoardService boardService = mock(BoardService.class);

   /* @Override
    protected Object initController() {
        return new BoardController(boardService);
    }

    //TODO 이미지 관련해서 왜 isOk가 안되는지 헤더 확인하기
    //TODO 시작할 때 커밋을 한 후 updateProject -> push 순으로 작업 시작하기
    @Test
    void 동아리공지_등록_테스트() throws Exception {
        NoticeClubRequestDTO requestDTO = NoticeClubRequestDTO.builder()
                .boardType(BoardType.NOTICE_CLUB)
                .title("제목데스")
                .content("내용데스")
                .writer("글쓴이데스")
                .build();
        //given의 경우 서비스 어떤 작업 수행 -> willReturn으로 결과 지정
        given(boardService.saveNoticeClub(requestDTO, null)).willReturn(true);

        mockMvc.perform(post("/api/saveNoticeClub") //post 매핑에 라우터 정보 넣기
                        .contentType(MediaType.MULTIPART_FORM_DATA) //헤더에 미디어 타입 정의
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))) //보내주는 정보에 대해서 String으로 포맷 맞춰서 넣어줌.
                .andDo(print()) //요청에 대해서 작성
                .andExpect(status().isOk()) //status().isOk()의 경우 클라이언트로부터 정상 수신했다는 이야기
                .andDo(document("club-notice-save", //docs에 club-notice-save라는 식별자 (Like 객체 선언) 로 나중에 적어줌.
                        preprocessRequest(prettyPrint()), //요청에 관련해서 이쁘게 출력
                        preprocessResponse(prettyPrint()) // 응답에 관련해서 이쁘게 출력
                )); //추가로 필드 관련해서 각 어떤 걸 의미하는지 정의해주고 싶으면 다른 사람 한 거 참고해서 작성하기.
    }


    @Test
    void 부원모집_등록_테스트() throws Exception {
        RecruitMemberRequestDTO reuestDTO;
    }

    @Test
    void 활동사진_등록_테스트() throws Exception {
        ActivityPhotoRequestDTO reuestDTO;
    }

    @Test
    void 활동영상_등록_테스트() throws Exception {
        ActivityVideoRequestDTO reuestDTO;
    }*/
}