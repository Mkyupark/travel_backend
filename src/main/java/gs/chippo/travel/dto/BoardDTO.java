package gs.chippo.travel.dto;


import gs.chippo.travel.entity.BoardEntity;
import gs.chippo.travel.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDTO {
    private String id;
    private String title;
    private String content;
    private boolean fix;
    private String userId;
    private UserEntity user;

    // entity => DTO로 삽입시
    public BoardDTO (final BoardEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.fix = entity.isFix();
    }

    public static BoardEntity toEntity(final BoardDTO boardDTO){
        return BoardEntity.builder()
                .id(boardDTO.getId())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .fix(boardDTO.isFix()).build();
    }

}
