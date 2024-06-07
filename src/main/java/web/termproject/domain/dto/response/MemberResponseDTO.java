package web.termproject.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {
    private Long id;
    private String loginId;
    private String loginPw;
    private String name;
    private String stuNum;
    private String major;
    private String phoneNum;
    private String email;
    private String gender;
    private String birthDate;
}