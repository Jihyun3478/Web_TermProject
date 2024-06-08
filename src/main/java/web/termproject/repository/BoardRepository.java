package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
