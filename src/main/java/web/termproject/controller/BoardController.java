package web.termproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.dto.response.board.ActivityPhotoResponseDTO;
import web.termproject.domain.dto.response.board.ActivityVideoResponseDTO;
import web.termproject.domain.dto.response.board.NoticeClubResponseDTO;
import web.termproject.domain.dto.response.board.RecruitMemberResponseDTO;
import web.termproject.service.BoardService;

import java.util.List;

@RestController
@RequestMapping("/master/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //동아리 공지 게시글 등록
    @PostMapping("/api/saveNoticeClub")
    public ResponseEntity<String> save(
            @RequestPart("noticeClubRequestDTO") @Valid NoticeClubRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Boolean isSaved = boardService.saveNoticeClub(boardRequestDTO, image);

        if (isSaved) {
            return ResponseEntity.ok("동아리 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("동아리 게시글 등록 실패");
        }
    }

    //부원 모집 게시글 등록
    @PostMapping("/api/saveRecruitMember")
    public ResponseEntity<String> saveRecruitMember(
            @RequestPart("recruitMemberRequestDTO") @Valid RecruitMemberRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Boolean isSaved = boardService.saveRecruitMember(boardRequestDTO, image);

        if (isSaved) {
            return ResponseEntity.ok("부원 모집 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("부원 모집 게시글 등록 실패");
        }
    }

    //활동 사진 게시글 등록
    @PostMapping("/api/saveActivityPhoto")
    public ResponseEntity<String> saveActivityPhoto(
            @RequestPart("activityPhotoRequestDTO") @Valid ActivityPhotoRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Boolean isSaved = boardService.saveActivityPhoto(boardRequestDTO, image);

        if (isSaved) {
            return ResponseEntity.ok("활동 사진 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("활동 사진 게시글 등록 실패");
        }
    }

    //활동 영상 게시글 등록
    @PostMapping("/api/saveActivityVideo")
    public ResponseEntity<String> saveActivityVideo(
            @RequestPart("activityPhotoRequestDTO") @Valid ActivityVideoRequestDTO boardRequestDTO) {

        Boolean isSaved = boardService.saveActivityVideo(boardRequestDTO);

        if (isSaved) {
            return ResponseEntity.ok("활동 영상 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("활동 영상 게시글 등록 실패");
        }
    }


    //동아리 공지 특정 게시글 조회
    @GetMapping("/noticeClub/{boardId}")
    public NoticeClubResponseDTO getBoard(@PathVariable("boardId") Long boardId){
        return boardService.getAnnouncement(boardId);
    }

    //동아리 공지 전체 게시글 조회 -> 동아리 회원만
    @GetMapping("/noticeClub/findAll")
    public List<NoticeClubResponseDTO> findAllNoticeClub(){
        return boardService.findAllAnnouncement();
    }

    //부원 모집 게시글 전체 조회
    @GetMapping("/recruitMember/findAll")
    public List<RecruitMemberResponseDTO> findAllRecruitMember(){
        return boardService.findAllRecruitMember();
    }

    //부원 모집 특정 게시글 조회
    @GetMapping("/recruitMember/{boardId}")
    public RecruitMemberResponseDTO getRecruitMember(@PathVariable("boardId") Long boardId){
        return boardService.getRecruitMember(boardId);
    }

    //활동 사진 게시글 전체 조회
    @GetMapping("/activityPhoto/findAll")
    public List<ActivityPhotoResponseDTO> findAllActivityPhoto(){
        return boardService.findAllActivityPhoto();
    }

    //활동 사진 특정 게시글 조회
    @GetMapping("/activityPhoto/{boardId}")
    public ActivityPhotoResponseDTO getActivityPhoto(@PathVariable("boardId") Long boardId){
        return boardService.getActivityPhoto(boardId);
    }

    //활동 영상 게시글 전체 조회
    @GetMapping("/activityVideo/findAll")
    public List<ActivityVideoResponseDTO> findAllActivityVideo(){
        return boardService.findAllActivityVideo();
    }

    //활동 영상 특정 게시글 조회
    @GetMapping("/activityVideo/{boardId}")
    public ActivityVideoResponseDTO getActivityVideo(@PathVariable("boardId") Long boardId){
        return boardService.getActivityVideo(boardId);
    }
}
