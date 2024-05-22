package web.termproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.termproject.domain.entity.ApplyMember;
import web.termproject.domain.entity.Club;
import web.termproject.domain.entity.Member;

public interface ApplyMemberRepository extends JpaRepository<ApplyMember, Long> {
    boolean existsApplyMemberByMemberId(Long memberId);

    @Query("SELECT c FROM Club c WHERE c.id = :clubId")
    Club findClubByClubId(@Param("clubId") Long clubId);

    @Query("SELECT m FROM Member m WHERE m.id = :memberId")
    Member findMemberByMemberId(@Param("memberId") Long memberId);
}
