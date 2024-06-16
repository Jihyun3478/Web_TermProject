package web.termproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;
import web.termproject.repository.BoardRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private static String uploadDirectory = System.getProperty("user.home") + "/Desktop/uploads";


    //동아리 공지 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    @Override
    public Boolean saveNoticeClub(NoticeClubRequestDTO boardRequestDTO, MultipartFile image) {
        Board board = boardRequestDTO.toEntity();

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

        boardRepository.save(board);

        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }

    //부원 모집 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    @Override
    public Boolean saveRecruitMember(RecruitMemberRequestDTO boardRequestDTO, MultipartFile image) {
        Board board = boardRequestDTO.toEntity();

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

        boardRepository.save(board);
        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }

    //활동 사진 등록
    @Override
    public Boolean saveActivityPhoto(ActivityPhotoRequestDTO boardRequestDTO, MultipartFile image) {
        Board board = boardRequestDTO.toEntity();

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

        boardRepository.save(board);
        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }

    //활동 영상 등록
    @Override
    public Boolean saveActivityVideo(ActivityVideoRequestDTO boardRequestDTO) {
        Board board = boardRequestDTO.toEntity();
        boardRepository.save(board);
        // 저장된 게시글의 ID가 null이 아닌지 확인하여 Boolean 값 반환
        return board.getId() != null;
    }


    // 동아리 공지 게시글 전체 조회
    public List<NoticeClubResponseDTO> findAllAnnouncement() {
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
    private NoticeClubResponseDTO getNoticeClubResponseDTO(Board board) {
        NoticeClubResponseDTO dto = new NoticeClubResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMember(board.getMember());
        dto.setBoardType(board.getBoardType());
        dto.setImageRoute(board.getImageRoute());
        return dto;
    }

    //부원 모집
    private RecruitMemberResponseDTO getRecruitMemberResponseDTO(Board board) {
        RecruitMemberResponseDTO dto = new RecruitMemberResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMember(board.getMember());
        dto.setBoardType(board.getBoardType());
        dto.setImageRoute(board.getImageRoute());
        return dto;
    }

    //활동 사진
    private ActivityPhotoResponseDTO getActivityPhotoResponseDTO(Board board) {
        ActivityPhotoResponseDTO dto = new ActivityPhotoResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMember(board.getMember());
        dto.setBoardType(board.getBoardType());
        dto.setImageRoute(board.getImageRoute());
        return dto;
    }

    //활동 영상
    private ActivityVideoResponseDTO getActivityVideoResponseDTO(Board board) {
        ActivityVideoResponseDTO dto = new ActivityVideoResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setVideoUrl(board.getContent());
        dto.setMember(board.getMember());
        dto.setBoardType(board.getBoardType());
        return dto;
    }
}
