package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;

import java.util.List;
import java.util.Optional;

public interface MasterRepository extends JpaRepository<Club, Long> {

    @Query("SELECT c " +
            "FROM Member m " +
            "JOIN m.applyClub ac " +
            "JOIN ac.professor p " +
            "JOIN ac.club c " +
            "WHERE m.id = :memberId AND " +
            "ac.applyClubStatus = 'ACCEPT' AND " +
            "ac.member.id = :memberId")
    List<Club> findClubsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT am " +
            "FROM ApplyMember am " +
            "JOIN am.club c " +
            "JOIN am.member m " +
            "WHERE c.id = :clubId")
    List<ApplyMember> findApplyMemberByClubId(@Param("clubId") Long clubId);

    @Query("SELECT c " +
            "FROM Club c " +
            "JOIN c.applyClub ac " +
            "JOIN ac.professor p " +
            "JOIN ac.member m " +
            "WHERE c.id = :clubId " +
            "AND ac.applyClubStatus = 'ACCEPT' " +
            "AND ac.member.id = :memberId")
    Optional<Club> findClubByClubIdAndMemberId(@Param("clubId") Long clubId, @Param("memberId") Long memberId);

    @Query("SELECT am " +
            "FROM ApplyMember am " +
            "WHERE am.id = :applyMemberId")
    Optional<ApplyMember> findApplyMemberByApplyMemberId(@Param("applyMemberId") Long applyMemberId);
}
