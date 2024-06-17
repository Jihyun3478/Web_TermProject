package web.termproject.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import web.termproject.domain.status.RoleType;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;

    private String name;

    @Column(unique = true)
    private String stuNum;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false, unique = true)
    private String phoneNum;

    @Column(unique = true)
    private String email;
    private String gender;
    private String birthDate;

    @Enumerated(STRING)
    @Column(nullable = false)
    private RoleType role;

    private String grantType;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private ApplyClub applyClub;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ApplyMember> applyMemberList = new ArrayList<>();

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.loginPw = passwordEncoder.encode(loginPw);
    }
  
    public void addMemberAuthority() {
        this.role = RoleType.MEMBER;
    }

    public void addAdminAuthority() {
        this.role = RoleType.ADMIN;
    }
}
