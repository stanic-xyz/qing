package chenyunlong.zhangli.model.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户评论信息
 *
 * @author Stan
 */
@Data
public class AnimeComment implements Serializable {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createTime;
    private Long mid;
}
