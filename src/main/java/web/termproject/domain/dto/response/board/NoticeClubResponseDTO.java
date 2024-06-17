package web.termproject.domain.dto.response.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.BoardType;

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
}
