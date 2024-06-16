package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Professor findByLoginId(String loginId);
    Professor findByName(String name);
}
