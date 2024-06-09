package web.termproject.domain.dto.response;

import lombok.*;
import web.termproject.domain.status.ClubType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponseDTO {
    private Long id;
    private ClubType clubType;
    private String name;
    private String introduce;
    private String history;
    private String imageRoute;
    private Date meetingTime;
    private String president;
    private String vicePresident;
    private String generalAffairs;
    private ProfessorResponseDTO professor;
    private List<ApplyMemberReponseDTO> applyMember;
    private MemberResponseDTO MasterMember;
}
