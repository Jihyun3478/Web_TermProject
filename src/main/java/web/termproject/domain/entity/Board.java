package web.termproject.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import web.termproject.domain.status.BoardType;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(unique = true)
    private String title;

    @Column(unique = true)
    private String writer;

    @Column(columnDefinition = "LONGTEXT", length = 2000)
    private String content;
    private BoardType boardType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    private String imageRoute;
    private String videoRoute;

}
