package web.termproject.domain.dto.response.board;

import lombok.*;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.BoardType;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityVideoResponseDTO {
    private Long id;
    private String title;
    private Member member;
    private String videoUrl;
    private BoardType boardType;
    private String writer;
}
