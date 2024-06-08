package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ApplyMemberRepository extends JpaRepository<ApplyMember, Long> {
    @Query("SELECT DISTINCT c, p, m\n" +
            "FROM Club c\n" +
            "JOIN c.applyClub ac\n" +
            "JOIN ac.professor p\n" +
            "JOIN ac.member m\n" +
            "WHERE c NOT IN (\n" +
            "    SELECT c2\n" +
            "    FROM Club c2\n" +
            "    JOIN c2.applyMemberList am\n" +
            "    WHERE am.member.id = :memberId\n" +
            ")\n" +
            "OR EXISTS (\n" +
            "    SELECT am\n" +
            "    FROM ApplyMember am\n" +
            "    WHERE am.club = c\n" +
            "    AND am.applyMemberStatus = 1\n" +
            ")")
    List<Club> findClubByNotMemberClubId(@Param("memberId") Long memberId);

    @Query("SELECT CASE WHEN COUNT(am) > 0 THEN TRUE ELSE FALSE END FROM ApplyMember am WHERE am.member.id = :memberId AND am.club.id = :clubId")
    boolean existsApplyMemberByMemberIdAndClubId(@Param("clubId") Long clubId, @Param("memberId") Long memberId);

}
