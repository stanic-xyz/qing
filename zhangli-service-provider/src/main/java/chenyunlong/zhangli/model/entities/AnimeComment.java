package chenyunlong.zhangli.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户评论信息
 *
 * @author Stan
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AnimeComment extends BaseEntity {
    private Long id;
    private Long cid;
    private String username;
    private String content;
    private String ipAddress;
}
