package web.termproject.domain.dto.request.board;

import lombok.*;
import web.termproject.domain.entity.Board;
import web.termproject.domain.status.BoardType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeClubRequestDTO {
    private String title;
    private String writer;
    private String content;
    private BoardType boardType;
    private String imageRoute;
    private Boolean isPublic;
    private Long clubId; // 동아리 ID 추가


    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .boardType(BoardType.NOTICE_CLUB)
                .imageRoute(imageRoute)
                .isPublic(isPublic)
                .build();
    }
}