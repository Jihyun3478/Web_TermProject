package web.termproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.board.ActivityPhotoRequestDTO;
import web.termproject.domain.dto.request.board.ActivityVideoRequestDTO;
import web.termproject.domain.dto.request.board.NoticeClubRequestDTO;
import web.termproject.domain.dto.request.board.RecruitMemberRequestDTO;
import web.termproject.domain.dto.response.board.ActivityPhotoResponseDTO;
import web.termproject.domain.dto.response.board.ActivityVideoResponseDTO;
import web.termproject.domain.dto.response.board.NoticeClubResponseDTO;
import web.termproject.domain.dto.response.board.RecruitMemberResponseDTO;
import web.termproject.domain.entity.Board;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.BoardType;
import web.termproject.repository.BoardRepository;
import web.termproject.repository.ClubRepository;
import web.termproject.repository.MemberRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import web.termproject.domain.status.RoleType;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private static String uploadDirectory = "C:\\Users\\82109\\Desktop\\uploads";



    //동아리 공지 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    @Override
    public Boolean saveNoticeClub(NoticeClubRequestDTO boardRequestDTO, MultipartFile image, String loginId) {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
      /*  Club club = clubRepository.findById(boardRequestDTO.getClubId())
                .orElseThrow(() -> new EntityNotFoundException("해당 동아리를 찾을 수 없습니다."));*/

        if (image != null && !image.isEmpty()) {
            try {
                String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDirectory, imageFileName);
                Files.write(imagePath, image.getBytes());
                board.setImageRoute(String.valueOf(imagePath));  // 이미지 경로 설정
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
        }

        board.setMember(member);
        board.setWriter(member.getName());
        board.setBoardType(BoardType.NOTICE_CLUB);
        board.setPublic(boardRequestDTO.isPublic()); // 공개 여부 설정

       // System.out.println("Board Type: " + board.getBoardType()); // 디버그용 로그 추가

        boardRepository.save(board);

        return board.getId() != null;
    }


    //부원 모집 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    @Override
    public Boolean saveRecruitMember(RecruitMemberRequestDTO boardRequestDTO, MultipartFile image, String loginId) {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
        // 이미지 파일 처리
        if (image != null && !image.isEmpty()) {
            try {
                String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDirectory, imageFileName);
                Files.write(imagePath, image.getBytes());
                board.setImageRoute(String.valueOf(imagePath));
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
        }
        board.setMember(member);
        board.setWriter(member.getName());
        board.setBoardType(BoardType.RECRUIT_MEMBER);
        boardRepository.save(board);
        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }

    //활동 사진 등록
    @Override
    public Boolean saveActivityPhoto(ActivityPhotoRequestDTO boardRequestDTO, MultipartFile image, String loginId) {
        Board board = boardRequestDTO.toEntity();
        Member member = memberService.findByLoginId(loginId);
        // 이미지 파일 처리
        if (image != null && !image.isEmpty()) {
            try {
                String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDirectory, imageFileName);
                Files.write(imagePath, image.getBytes());
                board.setImageRoute(String.valueOf(imagePath));
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
        }
        board.setMember(member);
        board.setWriter(member.getName());
        board.setBoardType(BoardType.ACTIVITY_PHOTO);
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



    // 동아리 공지 게시글 전체 조회
    @Override
    public List<NoticeClubResponseDTO> findAllAnnouncement() {
        // 사용자가 속한 모든 동아리의 이름 목록을 가져옵니다.
      //  List<String> clubNames = memberService.findClubNamesByLoginId(loginId);

        // 동아리 이름 목록을 이용하여 해당 동아리의 공지글만 필터링하여 조회합니다.
        List<Board> boards = boardRepository.findByBoardType(BoardType.NOTICE_CLUB);

        return boards.stream()
                .map(this::getNoticeClubResponseDTO)
                .collect(Collectors.toList());
    }




    // 동아리 공지 특정 게시글 조회
    @Override
    public NoticeClubResponseDTO getAnnouncement(Long boardId) {
        Board board = boardRepository.findByIdAndBoardType(boardId, BoardType.NOTICE_CLUB)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없거나 동아리 공지가 아닙니다."));

        return getNoticeClubResponseDTO(board);
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
        dto.setImageRoute(board.getImageRoute());
        dto.setRoleType(board.getMember().getRole());
/*
        // 동아리 이름 설정
        String clubName;
        if (board.getClubName() != null) {
            clubName = board.getClubName(); // Board 엔티티에 저장된 동아리 이름 사용
        } else {
            // 추가적인 로직이 필요하다면 여기에 구현
            clubName = ""; // 혹은 null 처리
        }
        dto.setClubName(clubName);*/

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
        dto.setImageRoute(board.getImageRoute());
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
        dto.setImageRoute(board.getImageRoute());
        dto.setRoleType(board.getMember().getRole());
        return dto;
    }

    //활동 영상
    private ActivityVideoResponseDTO getActivityVideoResponseDTO(Board board) {
        ActivityVideoResponseDTO dto = new ActivityVideoResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setVideoUrl(board.getContent());
        dto.setMemberId(board.getMember().getId());
        dto.setWriter(board.getMember().getName());
        dto.setBoardType(board.getBoardType());
        dto.setRoleType(board.getMember().getRole());
        return dto;
    }

    @Override
    public Resource getImage(String imagePath) throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory, imagePath).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found or cannot read file: " + imagePath);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path filePath = Paths.get(uploadDirectory, filename).normalize();
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

    private boolean isClubMember(String loginId, Board board) {
        // 사용자 ID를 이용하여 회원 정보 조회
        Member member = memberRepository.findByLoginId(loginId);
        if (member == null) {
            return false; // 회원 정보가 없으면 false 반환
        }

        // Board에 연결된 Member와 조회된 Member가 동일한지 확인
        return board.getMember().equals(member);
    }

}
