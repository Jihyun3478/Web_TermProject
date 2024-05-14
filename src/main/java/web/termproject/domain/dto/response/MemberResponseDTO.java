package web.termproject.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDTO {
    private String name;
    private String stuNum;
    private String major;
    private String phoneNum;
    private String email;
    private String gender;
}
