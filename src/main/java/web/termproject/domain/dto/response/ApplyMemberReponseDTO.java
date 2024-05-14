package web.termproject.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.ApplyMemberStatus;

@Getter
@Setter
public class ApplyMemberReponseDTO {
    private Long id;
    private ApplyMemberStatus applyMemberStatus;
    private MemberResponseDTO member;
}
