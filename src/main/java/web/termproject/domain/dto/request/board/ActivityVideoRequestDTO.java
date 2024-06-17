package web.termproject.domain.dto.request.board;

import lombok.*;
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityVideoRequestDTO {
    private String title;
    private String videoUrl;
    private BoardType boardType;
    private String writer;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(videoUrl)
                .boardType(BoardType.ACTIVITY_VIDEO)
                .build();
    }
}
