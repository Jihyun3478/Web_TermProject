package web.termproject.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import web.termproject.domain.entity.Professor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PSignupRequestDTO {

    @NotBlank
    @Size(min = 6,max = 10, message ="아이디는 6자 이상 10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "작성자명은 알파벳 소문자, 숫자만 사용 가능합니다.")
    private String pLoginId;

    @NotBlank
    @Size(min = 8,max = 12, message ="비밀번호는 8자 이상 12자 이하만 가능합니다.")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "작성자명은 알파벳 대문자, 소문자, 숫자만 사용 가능합니다.")
    private String loginPw;

    @NotBlank
    private String name;

    @NotBlank
    private String major;

    @NotBlank
    private String phoneNum;

    @NotBlank
    private String email;

    public Professor toEntity() {
        return Professor.builder()
                .loginId(pLoginId)
                .loginPw(loginPw)
                .name(name)
                .major(major)
                .phoneNum(phoneNum)
                .email(email)
                .build();
    }
}
