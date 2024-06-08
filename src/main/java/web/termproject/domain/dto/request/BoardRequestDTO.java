package web.termproject.domain.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import web.termproject.domain.entity.Board;
import web.termproject.domain.entity.Member;
import web.termproject.domain.status.BoardType;
import web.termproject.domain.status.RoleType;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDTO {
    private String title;
    private String writer;
    private String content;
    private BoardType boardType;
    //private Member member;
    private String imageRoute;
    private String videoRoute;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .boardType(boardType)
              // .member(member)
                .imageRoute(imageRoute)
                .videoRoute(videoRoute)
                .build();
    }
}