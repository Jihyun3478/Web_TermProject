package web.termproject.domain.dto.response;

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
public class BoardResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Member member;
    private BoardType boardType;
    private String imageRoute;
    private String videoRoute;
    private MultipartFile image;
}
