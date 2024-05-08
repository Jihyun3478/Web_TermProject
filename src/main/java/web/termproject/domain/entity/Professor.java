package web.termproject.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFESSOR_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String loginPw;

    @Column(nullable = false)
    private String major;

    @Column(unique = true)
    private String phoneNum;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @OneToOne(mappedBy = "professor", fetch = FetchType.LAZY)
    private Club club;
}
