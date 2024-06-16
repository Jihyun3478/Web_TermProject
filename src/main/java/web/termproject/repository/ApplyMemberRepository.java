package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;

import java.util.List;

public interface ApplyMemberRepository extends JpaRepository<ApplyMember, Long> {

    @Query("SELECT DISTINCT c " +
            "FROM Club c " +
            "JOIN c.applyClub ac " +
            "JOIN ac.professor p " +
            "JOIN ac.member m " +
            "WHERE c NOT IN (" +
            "    SELECT c2 " +
            "    FROM Club c2 " +
            "    JOIN c2.applyMemberList am " +
            "    WHERE am.member.id = :memberId" +
            ") " +
            "OR EXISTS (" +
            "    SELECT am " +
            "    FROM ApplyMember am " +
            "    WHERE am.club = c " +
            "    AND am.applyMemberStatus = 'APPROVED'" +
            ") " +
            "AND ac.applyClubStatus = 'ACCEPT'")
    List<Club> findClubByNotMemberClubId(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(am) > 0 THEN TRUE ELSE FALSE END " +
            "FROM ApplyMember am " +
            "WHERE am.member.id = :memberId " +
            "AND am.club.id = :clubId")
    boolean existsApplyMemberByMemberIdAndClubId(@Param("clubId") Long clubId, @Param("memberId") Long memberId);
}
