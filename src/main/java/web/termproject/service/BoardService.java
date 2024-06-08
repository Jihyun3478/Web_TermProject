package web.termproject.service;

import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.BoardRequestDTO;
import web.termproject.domain.dto.response.BoardResponseDTO;

import java.util.List;

public interface BoardService {
    void save(BoardRequestDTO boardRequestDTO, MultipartFile image);

    List<BoardResponseDTO> findAllAnnouncement();

    // 동아리 공지 특정 게시글 조회
    BoardResponseDTO getAnnouncement(Long boardId);

    //나머지 게시글 전체조회
    List<BoardResponseDTO> findAll();

    //나머지 특정 게시글 조회
    BoardResponseDTO getBoard(Long boardId);
}
