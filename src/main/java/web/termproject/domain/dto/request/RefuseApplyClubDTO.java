package web.termproject.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefuseApplyClubDTO {
    private String refuseReason;
}
