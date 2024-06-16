package web.termproject.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefuseApplyClubDTO {
    private String refuseReason;

    @JsonCreator
    public RefuseApplyClubDTO(@JsonProperty("refuseReason") String refuseReason) {
        this.refuseReason = refuseReason;
    }
}
