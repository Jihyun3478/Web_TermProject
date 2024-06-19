package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.dto.response.board.NoticeClubResponseDTO;
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByBoardType(BoardType boardType);
  //  List<Board> findByIsPublicTrue();
    Optional<Board> findByIdAndBoardType(Long id, BoardType boardType);
    //findByClub_IdIn는 club 객체의 id 필드를 대상으로 IN 연산을 수행하는 쿼리를 생성합니다.
    List<Board> findByClub_IdIn(List<Long> clubIds);

    List<Board> findByBoardTypeAndClubNameIn(BoardType boardType, List<String> clubNames);


    List<Board> findByIsPublic(boolean b);

  List<Board> findByMemberId(Long memberId);
}

