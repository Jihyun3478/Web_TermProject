package web.termproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.dto.request.BoardRequestDTO;
import web.termproject.domain.dto.response.*;
import web.termproject.domain.entity.Board;
import web.termproject.repository.BoardRepository;
import web.termproject.repository.MemberRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private static String uploadDirectory = System.getProperty("user.home") + "/Desktop/uploads";


    //게시글 등록 -> 모든 게시글에 이미지 및 동영상 등록 가능
    public void save(BoardRequestDTO boardRequestDTO, MultipartFile image) {
        // Member member = memberRepository.findByLoginId(SecurityUtil.getLoginId()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        Board board = boardRequestDTO.toEntity();

        // 이미지 파일 처리
        if (image != null && !image.isEmpty()) {
            try {
                String imageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDirectory, imageFileName);
                Files.write(imagePath, image.getBytes());
                board.setImageRoute(imageFileName);
            } catch (IOException e) {
                throw new RuntimeException("이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
        }

        // 동영상 URL은 이미 DTO에서 엔티티로 매핑됨
        /* board.setMember(member);
        board.setWriter(member.getName()); */
        // board.setVideoRoute(boardRequestDTO.getVideoRoute());
        boardRepository.save(board);
    }

    // 동아리 공지 게시글 전체 조회
    @Override
    public List<BoardResponseDTO> findAllAnnouncement() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::getBoardResponseDTO)
                .collect(Collectors.toList());
    }

    // 동아리 공지 특정 게시글 조회
    @Override
    public BoardResponseDTO getAnnouncement(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        return board.map(this::getBoardResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    //나머지 게시글 전체조회
    @Override
    public List<BoardResponseDTO> findAll() {
        List<Board> boards = boardRepository.findAll();
        return boards.stream()
                .map(this::getBoardResponseDTO)
                .collect(Collectors.toList());
    }

    //나머지 특정 게시글 조회
    @Override
    public BoardResponseDTO getBoard(Long boardId) {
        Optional<Board> board = boardRepository.findById(boardId);
        return board.map(this::getBoardResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    private BoardResponseDTO getBoardResponseDTO(Board board) {
        BoardResponseDTO dto = new BoardResponseDTO();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setMember(board.getMember());
        dto.setBoardType(board.getBoardType());
        dto.setImageRoute(board.getImageRoute());
        dto.setVideoRoute(board.getVideoRoute());
        return dto;
    }
}
