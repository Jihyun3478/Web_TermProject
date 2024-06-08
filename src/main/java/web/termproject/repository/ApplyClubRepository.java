package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.entity.ApplyClub;

public interface ApplyClubRepository extends JpaRepository<ApplyClub, Long> {
}
