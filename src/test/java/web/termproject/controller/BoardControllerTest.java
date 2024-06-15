package web.termproject.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import web.termproject.docs.RestDocsSupport;
import web.termproject.domain.dto.request.BoardRequestDTO;
import web.termproject.domain.dto.response.ApplyClubResponseDTO;
import web.termproject.domain.dto.response.BoardResponseDTO;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.BoardType;
import web.termproject.domain.status.ClubType;
import web.termproject.service.ApplyClubService;
import web.termproject.service.BoardService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
public class BoardControllerTest {

    @Autowired
    private BoardService boardService;

    @Test
    void 동아리_공지글_테스트() {
        BoardRequestDTO requestDTO = BoardRequestDTO.builder()
                .boardType(BoardType.NOTICE_CLUB)
                .title("제목데스")
                .content("내용데스")
                .writer("글쓴이데스")
                .build();

        assertThat(true).isEqualTo(boardService.save(requestDTO,null));

    }
}