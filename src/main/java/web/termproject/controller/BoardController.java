package web.termproject.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import web.termproject.domain.dto.request.ClubRequestDTO;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.dto.response.board.ActivityPhotoResponseDTO;
import web.termproject.domain.dto.response.board.ActivityVideoResponseDTO;
import web.termproject.domain.dto.response.board.NoticeClubResponseDTO;
import web.termproject.domain.dto.response.board.RecruitMemberResponseDTO;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Board;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.RoleType;
import web.termproject.repository.ApplyMemberRepository;
import web.termproject.repository.BoardRepository;
import web.termproject.repository.MemberRepository;
import web.termproject.security.util.SecurityUtil;
import web.termproject.service.BoardService;
import web.termproject.service.MemberService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/master/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ApplyMemberRepository applyMemberRepository;
    private final BoardRepository boardRepository;
    private static final String uploadDirectory = "C:\\Users\\82109\\Desktop\\uploads";


    //동아리 공지 게시글 등록
    @PostMapping(value="/api/saveNoticeClub",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> save(
            @RequestPart("noticeClubRequestDTO") @Valid NoticeClubRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        String loginId = SecurityUtil.getLoginId();
       Member member = memberService.findByLoginId(loginId);

        if (member.getRole() != RoleType.MASTER_MEMBER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to post notices.");
        }

        Boolean isSaved = boardService.saveNoticeClub(boardRequestDTO, image, loginId);

        if (isSaved) {
            return ResponseEntity.ok("동아리 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("동아리 게시글 등록 실패");
        }
    }

    //부원 모집 게시글 등록
    @PostMapping(value="/api/saveRecruitMember",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveRecruitMember(
            @RequestPart("recruitMemberRequestDTO") @Valid RecruitMemberRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberService.findByLoginId(loginId);

        if (member.getRole() != RoleType.MASTER_MEMBER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to post notices.");
        }
        Boolean isSaved = boardService.saveRecruitMember(boardRequestDTO, image,loginId);

        if (isSaved) {
            return ResponseEntity.ok("부원 모집 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("부원 모집 게시글 등록 실패");
        }
    }

    //활동 사진 게시글 등록
    @PostMapping(value="/api/saveActivityPhoto",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveActivityPhoto(
            @RequestPart("activityPhotoRequestDTO") @Valid ActivityPhotoRequestDTO boardRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberService.findByLoginId(loginId);

        if (member.getRole() != RoleType.MASTER_MEMBER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to post notices.");
        }
        Boolean isSaved = boardService.saveActivityPhoto(boardRequestDTO, image,loginId);

        if (isSaved) {
            return ResponseEntity.ok("활동 사진 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("활동 사진 게시글 등록 실패");
        }
    }

    //활동 영상 게시글 등록
    @PostMapping("/api/saveActivityVideo")
    public ResponseEntity<String> saveActivityVideo(
            @RequestBody ActivityVideoRequestDTO boardRequestDTO) {
        String loginId = SecurityUtil.getLoginId();
        Member member = memberService.findByLoginId(loginId);
        log.info("url : {}",boardRequestDTO.getContent());
        if (member.getRole() != RoleType.MASTER_MEMBER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to post notices.");
        }
        Boolean isSaved = boardService.saveActivityVideo(boardRequestDTO,loginId);

        if (isSaved) {
            return ResponseEntity.ok("활동 영상 게시글 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("활동 영상 게시글 등록 실패");
        }
    }


    //동아리 공지 특정 게시글 조회
    @GetMapping("/noticeClub/{id}")
    public ResponseEntity<NoticeClubResponseDTO> getNoticeClubById(@PathVariable Long id) {
        try {
            NoticeClubResponseDTO noticeClub = boardService.getAnnouncement(id);
            return ResponseEntity.ok(noticeClub);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //동아리 공지 전체 게시글 조회
    @GetMapping("/noticeClub/findAll")
    public List<NoticeClubResponseDTO> findAllNoticeClub() {
        // 현재 로그인한 사용자 정보 가져오기
        String loginId = SecurityUtil.getLoginId();
        Member member = memberRepository.findByLoginId(loginId);
        log.info("getRole1 : {}",member.getRole());
        // 전체 공지글 조회
        List<NoticeClubResponseDTO> allAnnouncements = null;

        if (RoleType.MASTER_MEMBER==member.getRole()) {
            log.info("getRole2 : {}",member.getRole());
            // MASTER_MEMBER인 경우 자신이 작성한 공지글과 전체 공지글 조회
            allAnnouncements = boardService.findAllAnnouncementForMasterMember(member.getId());
        } else {
            // 일반 회원인 경우 자신이 속한 동아리의 공지글과 전체 공지글 조회
            allAnnouncements = boardService.findClubAnnouncements(member.getId());
        }

        // 중복 제거 후 반환
        return allAnnouncements.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    //부원 모집 게시글 전체 조회
    @GetMapping("/recruitMember/findAll")
    public List<RecruitMemberResponseDTO> findAllRecruitMember() {
        return boardService.findAllRecruitMember();
    }

    //부원 모집 특정 게시글 조회
    @GetMapping("/recruitMember/{boardId}")
    public RecruitMemberResponseDTO getRecruitMember(@PathVariable("boardId") Long boardId){
        return boardService.getRecruitMember(boardId);
    }

    //활동 사진 게시글 전체 조회
    @GetMapping("/activityPhoto/findAll")
    public List<ActivityPhotoResponseDTO> findAllActivityPhoto() {
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

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find the board with id: " + id));

        byte[] imageBytes = board.getPhoto();

        if (imageBytes != null && imageBytes.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } else {
            throw new RuntimeException("Could not find the image for board with id: " + id);
        }
    }
    @GetMapping("/checkMasterMember")
    public ResponseEntity<Boolean> checkMasterMember() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        // 사용자의 역할(Role) 확인
        Member member = memberService.findByLoginId(loginId);
        boolean isMasterMember = member.getRole() == RoleType.MASTER_MEMBER;

        // 마스터 회원 여부를 클라이언트에게 반환
        return ResponseEntity.ok(isMasterMember);
    }


    @GetMapping("/api/member/clubNames")
    public ResponseEntity<List<String>> getClubNames() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        // 사용자가 속한 동아리 이름 목록을 조회
        List<String> clubNames = memberService.findClubNamesByLoginId(loginId);

        return ResponseEntity.ok(clubNames);
    }
}
