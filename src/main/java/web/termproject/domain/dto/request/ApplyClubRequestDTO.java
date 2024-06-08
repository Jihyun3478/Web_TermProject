package web.termproject.domain.dto.request;

import lombok.Getter;
import web.termproject.domain.status.ClubType;

@Getter
public class ApplyClubRequestDTO {
    private ClubType clubType;
    private String clubName;
    private String loginId;
    private String professorLoginId;
}
