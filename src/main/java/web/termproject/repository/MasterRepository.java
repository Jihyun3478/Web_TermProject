package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.termproject.domain.entity.Club;

import java.util.List;

public interface MasterRepository extends JpaRepository<Club, Long> {
    @Query("SELECT DISTINCT c FROM Club c JOIN FETCH c.applyClubList ac JOIN ac.member m WHERE m.id = :memberId")
    List<Club> findClubsByMemberId(@Param("memberId") Long memberId);
}
