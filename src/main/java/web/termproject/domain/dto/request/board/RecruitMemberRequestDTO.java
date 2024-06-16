package web.termproject.domain.dto.request.board;

import lombok.*;
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecruitMemberRequestDTO {
    private String title;
    private String writer;
    private String content;
    private BoardType boardType;
    private String imageRoute;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .boardType(boardType)
                .imageRoute(imageRoute)
                .build();
    }
}
