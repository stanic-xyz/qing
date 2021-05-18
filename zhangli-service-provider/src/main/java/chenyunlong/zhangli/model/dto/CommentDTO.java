package chenyunlong.zhangli.model.dto;

import chenyunlong.zhangli.model.dto.base.OutputConverter;
import chenyunlong.zhangli.model.entities.AnimeComment;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Stan
 */
@Data
public class CommentDTO implements OutputConverter<CommentDTO, AnimeComment> {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createTime;
    private Long mid;

}
