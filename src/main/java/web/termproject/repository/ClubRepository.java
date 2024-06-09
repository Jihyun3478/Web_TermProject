package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.entity.Club;

public interface ClubRepository extends JpaRepository<Club, Long> {

}
