package web.termproject.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import web.termproject.domain.status.ApplyClubStatus;
import web.termproject.domain.status.ClubType;

@Getter
@Builder
public class ApplyClubResponseDTO {
    private Long applyClubId;
    private ApplyClubStatus applyClubStatus;
    private ClubType clubType;
    private String clubName;
    private String name;
    private String major;
    private String stuNum;
    private String phoneNum;
    private String pName;
    private String pMajor;
    private String pPhoneNum;
    private String refuseReason;
}
