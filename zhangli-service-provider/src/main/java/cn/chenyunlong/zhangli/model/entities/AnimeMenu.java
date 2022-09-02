package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AnimeMenu extends BaseEntity<AnimeMenu> {
    private String id;
    private String name;
    private String path;
}
