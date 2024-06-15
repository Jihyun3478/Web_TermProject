package web.termproject.domain.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.*;
import web.termproject.domain.entity.ApplyClub;

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
