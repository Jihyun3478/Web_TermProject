package web.termproject.domain.dto.response.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.entity.ApplyClub;
import web.termproject.domain.status.BoardType;
import web.termproject.domain.status.RoleType;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeClubResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long memberId;
    private BoardType boardType;
    private String imageRoute;
    private MultipartFile image;
    private String imageData;
    private RoleType roleType;
    private Boolean isPublic;
    private String clubName;
    private Long clubId;
}
