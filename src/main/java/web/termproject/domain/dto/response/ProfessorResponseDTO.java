package web.termproject.domain.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import web.termproject.domain.entity.ApplyClub;

@Getter
@Setter
public class ProfessorResponseDTO {
    private Long id;
    private String major;
    private String phoneNum;
    private String email;
    private String name;
}
