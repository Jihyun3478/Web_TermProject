package web.termproject.domain.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorResponseDTO {
    private Long id;
    private String major;
    private String phoneNum;
    private String email;
    private String name;
}
