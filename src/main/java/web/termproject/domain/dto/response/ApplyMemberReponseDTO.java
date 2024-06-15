package web.termproject.domain.dto.response;

import lombok.*;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.ApplyMemberStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyMemberReponseDTO {
    private Long id;
    private ApplyMemberStatus applyMemberStatus;
    private MemberResponseDTO member;
}
