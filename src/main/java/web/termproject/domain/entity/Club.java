package web.termproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import web.termproject.domain.status.ApplyMemberStatus;
import web.termproject.domain.status.ClubType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLUB_ID")
    private Long id;
    private ClubType clubType;
    private String name;

    @Column(columnDefinition = "LONGTEXT", length = 2000)
    private String introduce;

    @Column(columnDefinition = "LONGTEXT", length = 2000)
    private String history;

    private String imageRoute;
    private Date meetingTime;
    private String president;
    private String vicePresident;
    private String generalAffairs;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Board> boards;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "apply_club_id")
    private ApplyClub applyClub;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ApplyMember> applyMemberList = new ArrayList<>();

    public void setApplyClub(ApplyClub applyClub) {
        this.applyClub = applyClub;
    }

    public Club createClub(ApplyClub applyClub) {
        Club club = Club.builder()
                .clubType(applyClub.getClubType())
                .name(applyClub.getClubName())
                .applyMemberList(new ArrayList<>()) // applyMemberList를 초기화합니다.
                .build();

        ApplyMember masterMember = ApplyMember.builder()
                .member(applyClub.getMember())
                .applyMemberStatus(ApplyMemberStatus.CLUB_MEMBER)
                .club(club)
                .build();
        club.getApplyMemberList().add(masterMember);

        return club;
    }

    public void updateImageRouteInfo(String imageRoute) {
        this.imageRoute = imageRoute;
    }
}
