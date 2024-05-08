package web.termproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String loginPw;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String stuNum;

    @Column(nullable = false)
    private String major;

    @Column(unique = true)
    private String phoneNum;

    @Column(unique = true)
    private String email;
    private String gender;
    private Date birthDate;

    @Enumerated(STRING)
    @Column(nullable = false, length = 30)
    private RoleType role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ApplyClub>  applyClubList = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Board board;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.loginPw = passwordEncoder.encode(loginPw);
    }
    public void addUserAuthority() {
        this.role = RoleType.USER;
    }
}
