package web.termproject.domain.entity;

        import com.fasterxml.jackson.annotation.JsonBackReference;
        import jakarta.persistence.*;
        import lombok.*;
        import web.termproject.domain.status.BoardType;

        import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    private String title;

    private String writer;

    @Column(columnDefinition = "LONGTEXT", length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Club club;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonBackReference
    private Member member;

    private String imageRoute;
    private String videoRoute;
    private Boolean isPublic;
}
