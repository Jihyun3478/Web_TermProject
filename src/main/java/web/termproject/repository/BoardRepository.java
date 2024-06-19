package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByBoardType(BoardType boardType);

    Optional<Board> findByIdAndBoardType(Long id, BoardType boardType);


    List<Board> findByBoardTypeAndClubNameIn(BoardType boardType, List<String> clubNames);


}

