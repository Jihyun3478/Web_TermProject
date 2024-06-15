package web.termproject.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyClub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLY_CLUB_ID")
    private Long id;

    private ClubType clubType;
    private String clubName;
    private ApplyClubStatus applyClubStatus;
    private String refuseReason;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToOne(mappedBy = "applyClub")
    private Club club;

    public void setId(Long id) {
        this.id = id;
    }

    public void setApplyClubStatus(ApplyClubStatus applyClubStatus) {
        this.applyClubStatus = applyClubStatus;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public static ApplyClub createApplyClub(ClubType clubType, String clubName, Member member, Professor professor) {
        return ApplyClub.builder()
                .applyClubStatus(ApplyClubStatus.WAIT)
                .clubType(clubType)
                .clubName(clubName)
                .member(member)
                .professor(professor)
                .build();
    }
}
