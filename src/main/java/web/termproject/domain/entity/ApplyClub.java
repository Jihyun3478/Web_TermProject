package web.termproject.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplyClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLY_CLUB_ID")
    private Long id;

    private ClubType clubType;
    private String clubName;
    private ApplyClubStatus applyClubStatus;
    private String refuseReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToOne(mappedBy = "applyClub")
    private Club club;
}
