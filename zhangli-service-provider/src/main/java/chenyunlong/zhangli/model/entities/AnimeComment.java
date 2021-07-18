package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
