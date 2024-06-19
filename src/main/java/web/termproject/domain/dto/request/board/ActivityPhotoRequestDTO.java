package web.termproject.domain.dto.request.board;

import lombok.*;
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityPhotoRequestDTO {
    private String title;
    private String writer;
    private String content;
    private BoardType boardType;
    private String imageRoute;
    private byte[] photo;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .boardType(BoardType.ACTIVITY_PHOTO)
                .imageRoute(imageRoute)
                .build();
    }
}
