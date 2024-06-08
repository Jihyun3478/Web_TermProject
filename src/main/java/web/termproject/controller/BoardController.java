package web.termproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.BoardRequestDTO;
import web.termproject.domain.dto.response.BoardResponseDTO;
import web.termproject.service.BoardService;
import web.termproject.service.ImageService;

import java.util.List;

@RestController
@RequestMapping("/master/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final ImageService imageService;

    //마스터 게시글 등록
    @PostMapping("/api/save")
    public ResponseEntity<String> save(
            @RequestPart("boardRequestDTO") @Valid BoardRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        boardService.save(boardRequestDTO, image);

        return ResponseEntity.ok("게시글 등록 완료");
    }

    //동아리 공지 게시글 조회 -> 동아리 회원만
    @GetMapping("/findAllAnnouncement/")
    public List<BoardResponseDTO> findAllAnnouncement(){

        return boardService.findAllAnnouncement();
    }

    //특정게시글 조회
    @GetMapping("/{boardId}")
    public BoardResponseDTO getBoard(@PathVariable("boardId") Long boardId){
        return boardService.getBoard(boardId);
    }

}
