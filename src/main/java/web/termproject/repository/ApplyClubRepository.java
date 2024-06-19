package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.status.ApplyClubStatus;

import java.util.List;
import java.util.Optional;

public interface ApplyClubRepository extends JpaRepository<ApplyClub, Long> {
    Optional<ApplyClub> findById(Long id);
    ApplyClub findByMemberId(Long memberId);
    Optional<ApplyClub> findByClubNameAndMember_StuNum(String clubName, String stuNum);
    List<ApplyClub> findByApplyClubStatus(ApplyClubStatus applyClubStatus);
}
