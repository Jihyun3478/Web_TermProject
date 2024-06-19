package web.termproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.dto.response.board.ActivityPhotoResponseDTO;
import web.termproject.domain.dto.response.board.ActivityVideoResponseDTO;
import web.termproject.domain.dto.response.board.NoticeClubResponseDTO;
import web.termproject.domain.dto.response.board.RecruitMemberResponseDTO;
import web.termproject.domain.entity.*;
import web.termproject.domain.status.BoardType;
import web.termproject.repository.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final ClubRepository clubRepository;
    private final ApplyClubRepository applyClubRepository;
    private final ApplyMemberRepository applyMemberRepository;
    private static final String UPLOAD_DIRECTORY = "C:\\Users\\82109\\Desktop\\Web_TermProject\\src\\main\\resources\\static\\boardImage\\";


    //동아리 공지 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    @Override
    public Boolean saveNoticeClub(NoticeClubRequestDTO boardRequestDTO, MultipartFile image, String loginId) throws IOException {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
        // 로그인한 사용자가 속한 applyMemberList 조회
//        ApplyClub applyClub = applyClubRepository.findByMemberId(member.getId());

        Club club = clubRepository.findById(boardRequestDTO.getClubId())
                .orElseThrow(() -> new EntityNotFoundException("해당 동아리를 찾을 수 없습니다."));

        // applyMemberList에서 동아리 ID 목록 추출
//        Long clubId  = applyClub.getClub().getId();
//        Club club = clubRepository.findById(clubId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 동아리를 찾을 수 없습니다."));

        board.setMember(member);
        board.setWriter(member.getName());
        board.setBoardType(BoardType.NOTICE_CLUB);
        board.setClub(club);
        if(image != null) {
            board.setPhoto(image.getBytes());
        }else {
            board.setPhoto(null);
        }
        boardRepository.save(board);

        return board.getId() != null;
    }


    //부원 모집 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    @Override
    public Boolean saveRecruitMember(RecruitMemberRequestDTO boardRequestDTO, MultipartFile image, String loginId) throws IOException {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
        // 이미지 파일 처리
        if (image != null && !image.isEmpty()) {
            try {
                String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIRECTORY, imageFileName);
                Files.write(imagePath, image.getBytes());
                board.setImageRoute(String.valueOf(imagePath));
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
        }
        board.setMember(member);
        board.setWriter(member.getName());
        board.setBoardType(BoardType.RECRUIT_MEMBER);
        if(image != null) {
            board.setPhoto(image.getBytes());
        } else {
            board.setPhoto(null);
        }
        boardRepository.save(board);
        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }

    //활동 사진 등록
    @Override
    public Boolean saveActivityPhoto(ActivityPhotoRequestDTO boardRequestDTO, MultipartFile image, String loginId) throws IOException {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
        // 이미지 파일 처리
        if (image != null && !image.isEmpty()) {
            try {
                String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get(UPLOAD_DIRECTORY, imageFileName);
                Files.write(imagePath, image.getBytes());
                board.setImageRoute(String.valueOf(imagePath));
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
        }
        board.setMember(member);
        board.setWriter(member.getName());
        board.setBoardType(BoardType.ACTIVITY_PHOTO);
        if(image != null) {
            board.setPhoto(image.getBytes());
        } else {
            board.setPhoto(null);
        }
        boardRepository.save(board);
        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }

    //활동 영상 등록
    @Override
    public Boolean saveActivityVideo(ActivityVideoRequestDTO boardRequestDTO, String loginId) {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
        board.setMember(member); // 작성자 ID를 사용하도록 변경
        board.setWriter(member.getName());
        board.setBoardType(BoardType.ACTIVITY_VIDEO);
        boardRepository.save(board);
        return board.getId() != null;
    }

//동아리 공지글 전체공개
public List<NoticeClubResponseDTO> findAllAnnouncementIncludingPublic(List<Long> clubIds) {
    List<NoticeClubResponseDTO> filteredAnnouncements = findAllAnnouncement(clubIds);

    // 전체공개 공지글 추가
    List<Board> publicAnnouncements = boardRepository.findByIsPublic(true);
    List<NoticeClubResponseDTO> publicAnnouncementDTOs = publicAnnouncements.stream()
            .map(board -> {
                NoticeClubResponseDTO dto = convertToNoticeClubResponseDTO(board);
                dto.setIsPublic(true); // Set isPublic to true explicitly for public announcements
                return dto;
            })
            .collect(Collectors.toList());

    // 중복 제거 후 반환
    List<NoticeClubResponseDTO> allAnnouncements = new ArrayList<>(filteredAnnouncements);
    allAnnouncements.addAll(publicAnnouncementDTOs);
    return allAnnouncements.stream()
            .distinct()
            .collect(Collectors.toList());
}

    // 동아리 공지 게시글 전체 조회
    public List<NoticeClubResponseDTO> findAllAnnouncement(List<Long> clubIds) {
        return boardRepository.findByClub_IdIn(clubIds)
                .stream()
                .map(this::convertToNoticeClubResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeClubResponseDTO> findClubAnnouncements(Long memberId) {
        // 해당 회원이 속한 동아리 조회
        List<ApplyMember> applyMembers = applyMemberRepository.findApplyMembersByMemberIdAndStatus(memberId);
        List<Long> clubIds = applyMembers.stream()
                .map(applyMember -> applyMember.getClub().getId())
                .collect(Collectors.toList());

        // 동아리 공지글 조회 (해당 동아리에 속한 공지글만)
        List<NoticeClubResponseDTO> clubAnnouncements = findAllAnnouncement(clubIds).stream()
                .filter(noticeClub -> noticeClub.getBoardType() == BoardType.NOTICE_CLUB && clubIds.contains(noticeClub.getClubId()))
                .collect(Collectors.toList());

        // 전체 공개된 공지글 조회
        List<NoticeClubResponseDTO> publicAnnouncements = findAllPublicAnnouncements().stream()
                .filter(noticeClub -> noticeClub.getBoardType() == BoardType.NOTICE_CLUB)
                .collect(Collectors.toList());

        // 중복 제거 후 반환
        List<NoticeClubResponseDTO> allAnnouncements = new ArrayList<>(clubAnnouncements);
        allAnnouncements.addAll(publicAnnouncements);
        return allAnnouncements.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<NoticeClubResponseDTO> findAllPublicAnnouncements() {
        List<Board> publicAnnouncements = boardRepository.findByIsPublic(true);
        return publicAnnouncements.stream()
                .map(this::convertToNoticeClubResponseDTO)
                .collect(Collectors.toList());
    }



    // 동아리 공지 특정 게시글 조회
    @Override
    public NoticeClubResponseDTO getAnnouncement(Long boardId) {
        Board board = boardRepository.findByIdAndBoardType(boardId, BoardType.NOTICE_CLUB)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없거나 동아리 공지가 아닙니다."));

        return getNoticeClubResponseDTO(board);
    }
    // Convert Board entity to NoticeClubResponseDTO

    private NoticeClubResponseDTO convertToNoticeClubResponseDTO(Board board) {
        if (board == null) {
            return null; // or handle differently based on your application's logic
        }
        NoticeClubResponseDTO dto = new NoticeClubResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setWriter(board.getWriter());
        dto.setMemberId(board.getMember() != null ? board.getMember().getId() : null);
        dto.setBoardType(board.getBoardType());
        if(board.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(board.getPhoto()));
        }else {
            dto.setPhotoBase64(null);
        }
        dto.setIsPublic(board.getIsPublic());
        dto.setClubId(board.getClub() != null ? board.getClub().getId() : null);
        dto.setClubName(board.getClub() != null ? board.getClub().getName() : null);
        return dto;
    }

    //마스터 전용 조회
    @Override
    public List<NoticeClubResponseDTO> findAllAnnouncementForMasterMember(Long memberId) {
        // MASTER_MEMBER인 경우 자신이 작성한 공지글과 전체 공지글 조회
        List<Board> boards = boardRepository.findByMemberId(memberId);
        for (Board board : boards) {
            log.info("boards = {}", board.getTitle());
        }
        List<NoticeClubResponseDTO> masterMemberAnnouncements = boards.stream()
                .filter(board -> board.getBoardType() == BoardType.NOTICE_CLUB)
                .map(this::convertToNoticeClubResponseDTO)
                .collect(Collectors.toList());

        // 추가적으로 전체 공지글(isPublic) 조회하여 추가
        List<Board> publicAnnouncements = boardRepository.findByIsPublic(true);
        List<NoticeClubResponseDTO> publicAnnouncementDTOs = publicAnnouncements.stream()
                .filter(board -> board.getBoardType() == BoardType.NOTICE_CLUB)
                .map(this::convertToNoticeClubResponseDTO)
                .collect(Collectors.toList());

        // 중복 제거 후 반환
        List<NoticeClubResponseDTO> allAnnouncements = new ArrayList<>(masterMemberAnnouncements);
        allAnnouncements.addAll(publicAnnouncementDTOs);

        return allAnnouncements.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    //부원모집 게시글 전체조회
    @Override
    public List<RecruitMemberResponseDTO> findAllRecruitMember() {
        List<Board> boards = boardRepository.findByBoardType(BoardType.RECRUIT_MEMBER);
        return boards.stream()
                .map(this::getRecruitMemberResponseDTO)
                .collect(Collectors.toList());
    }

    //부원모집 특정 게시글 조회
    @Override
    public RecruitMemberResponseDTO getRecruitMember(Long boardId) {
        Board board = boardRepository.findByIdAndBoardType(boardId, BoardType.RECRUIT_MEMBER)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없거나 부원모집 게시글이 아닙니다."));

        return getRecruitMemberResponseDTO(board);
    }

    //활동 사진 게시글 전체 조회
    @Override
    public List<ActivityPhotoResponseDTO> findAllActivityPhoto() {
        List<Board> boards = boardRepository.findByBoardType(BoardType.ACTIVITY_PHOTO);
        return boards.stream()
                .map(this::getActivityPhotoResponseDTO)
                .collect(Collectors.toList());
    }

    //활동 사진 특정 게시글 조회
    @Override
    public ActivityPhotoResponseDTO getActivityPhoto(Long boardId) {
        Board board = boardRepository.findByIdAndBoardType(boardId, BoardType.ACTIVITY_PHOTO)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없거나 활동사진 게시글이 아닙니다."));

        return getActivityPhotoResponseDTO(board);
    }

    //활동 영상 게시글 전체 조회
    @Override
    public List<ActivityVideoResponseDTO> findAllActivityVideo() {
        List<Board> boards = boardRepository.findByBoardType(BoardType.ACTIVITY_VIDEO);
        return boards.stream()
                .map(this::getActivityVideoResponseDTO)
                .collect(Collectors.toList());
    }

    //활동 영상 특정 게시글 조회
    @Override
    public ActivityVideoResponseDTO getActivityVideo(Long boardId) {
        Board board = boardRepository.findByIdAndBoardType(boardId, BoardType.ACTIVITY_VIDEO)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없거나 활동영상 게시글이 아닙니다."));

        return getActivityVideoResponseDTO(board);
    }

    //동아리 공지
    @Transactional
    public NoticeClubResponseDTO getNoticeClubResponseDTO(Board board) {
        NoticeClubResponseDTO dto = new NoticeClubResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMemberId(board.getMember().getId()); // Member의 id만 설정
        dto.setWriter(board.getMember().getName());
        dto.setBoardType(board.getBoardType());
        if(board.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(board.getPhoto()));
        }else {
            dto.setPhotoBase64(null);
        }
        dto.setIsPublic(board.getIsPublic());
        dto.setRoleType(board.getMember().getRole());
        return dto;
    }
    //부원 모집
    private RecruitMemberResponseDTO getRecruitMemberResponseDTO(Board board) {
        RecruitMemberResponseDTO dto = new RecruitMemberResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMemberId(board.getMember().getId());
        dto.setBoardType(board.getBoardType());
        dto.setWriter(board.getMember().getName());
        if(board.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(board.getPhoto()));
        }else {
            dto.setPhotoBase64(null);
        }
        dto.setRoleType(board.getMember().getRole());
        return dto;
    }

    //활동 사진
    private ActivityPhotoResponseDTO getActivityPhotoResponseDTO(Board board) {
        ActivityPhotoResponseDTO dto = new ActivityPhotoResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setBoardType(board.getBoardType());
        dto.setMemberId(board.getMember().getId());
        dto.setWriter(board.getMember().getName());
        if(board.getPhoto() != null) {
            dto.setPhotoBase64(Base64.getEncoder().encodeToString(board.getPhoto()));
        }else {
            dto.setPhotoBase64(null);
        }
        dto.setRoleType(board.getMember().getRole());
        return dto;
    }

    //활동 영상
    private ActivityVideoResponseDTO getActivityVideoResponseDTO(Board board) {
        ActivityVideoResponseDTO dto = new ActivityVideoResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMemberId(board.getMember().getId());
        dto.setWriter(board.getMember().getName());
        dto.setBoardType(board.getBoardType());
        dto.setRoleType(board.getMember().getRole());
        return dto;
    }

    // 이미지 조회
    @Override
    public Resource getImage(String imagePath) throws MalformedURLException {
        log.info("imageReout : {}",imagePath);
        Path filePath = Paths.get(UPLOAD_DIRECTORY).resolve(imagePath).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found " + imagePath);
        }
    }


    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIRECTORY, filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Could not read file: " + filename);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
