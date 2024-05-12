package web.termproject.domain.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.entity.ClubType;
import web.termproject.domain.entity.Professor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ClubResponseDTO {
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
