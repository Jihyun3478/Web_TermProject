package web.termproject.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;

@Entity
@Getter
@Setter
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
