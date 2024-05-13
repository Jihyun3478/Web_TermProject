package web.termproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web.termproject.domain.status.ClubType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "apply_club_id")
    private ApplyClub applyClub;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ApplyMember> applyMemberList = new ArrayList<>();
}
