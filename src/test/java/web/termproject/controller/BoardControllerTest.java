package web.termproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.dto.response.board.ActivityVideoResponseDTO;
import web.termproject.domain.status.BoardType;
import web.termproject.service.BoardService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
public class BoardControllerTest {

    @Autowired
    private BoardService boardService;

    @Test
    void 동아리공지_등록_테스트() {
        NoticeClubRequestDTO requestDTO = NoticeClubRequestDTO.builder()
                .boardType(BoardType.NOTICE_CLUB)
                .title("제목데스")
                .content("내용데스")
                .writer("글쓴이데스")
                .build();

        assertThat(true).isEqualTo(boardService.saveNoticeClub(requestDTO,null,null));

    }

    @Test
    void 부원모집_등록_테스트() {
        RecruitMemberRequestDTO requestDTO = RecruitMemberRequestDTO.builder()
                .boardType(BoardType.RECRUIT_MEMBER)
                .title("부원모집 제목")
                .content("부원모집 내용")
                .writer("이소림")
                .build();

        assertThat(true).isEqualTo(boardService.saveRecruitMember(requestDTO,null,null));

    }

    @Test
    void 활동사진_등록_테스트() {
        ActivityPhotoRequestDTO requestDTO = ActivityPhotoRequestDTO.builder()
                .boardType(BoardType.ACTIVITY_PHOTO)
                .title("활동사진 제목")
                .content("활동사진 내용")
                .writer("이소림")
                .build();

        assertThat(true).isEqualTo(boardService.saveActivityPhoto(requestDTO,null,null));

    }

    @Test
    void 활동영상_등록_테스트() {
        ActivityVideoRequestDTO requestDTO = ActivityVideoRequestDTO.builder()
                .boardType(BoardType.ACTIVITY_VIDEO)
                .title("활동영상 제목")
                .videoUrl("https://youtu.be/wCqV__ggsKs?si=TgS_MbVi1XNHI0iY")
                .writer("이소림")
                .build();

        assertThat(true).isEqualTo(boardService.saveActivityVideo(requestDTO,null));
    }
    @Test
    void 활동영상_조회() {
        ActivityVideoRequestDTO requestDTO = ActivityVideoRequestDTO.builder()
                .boardType(BoardType.ACTIVITY_VIDEO)
                .title("활동영상 제목1")
                .videoUrl("https://youtu.be/wCqV__ggsKs?si=TgS_MbVi1XNHI0iY")
                .writer("이소림")
                .build();

        ActivityVideoRequestDTO requestDTO2 = ActivityVideoRequestDTO.builder()
                .boardType(BoardType.ACTIVITY_VIDEO)
                .title("활동영상 제목2")
                .videoUrl("https://youtu.be/wCqV__ggsKs?si=TgS_MbVi1XNHI0iY")
                .writer("마라")
                .build();

        boolean result1 = boardService.saveActivityVideo(requestDTO);
        boolean result2 = boardService.saveActivityVideo(requestDTO2);

        if(result1 && result2) {
            List<ActivityVideoResponseDTO> resultList = boardService.findAllActivityVideo();
            assertThat(requestDTO.getTitle()).isEqualTo(resultList.get(0).getTitle());
            assertThat(requestDTO2.getTitle()).isEqualTo(resultList.get(1).getTitle());
        }
    }
}