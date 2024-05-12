package web.termproject.domain.dto.request;

import lombok.Getter;
import lombok.Setter;
import web.termproject.domain.entity.ClubType;
import web.termproject.domain.entity.Professor;

import java.util.Date;

@Getter
@Setter
public class ClubRequestDTO {
    private Long id;
    private ClubType clubType;
    private String name;
    private String introduce;
    private String imageRoute;
    private Date meetingTime;
    private String president;
    private String vicePresident;
    private String generalAffairs;
}
