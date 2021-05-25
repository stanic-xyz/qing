package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;

/**
 * @author Stan
 */
@Data
public class AnimeMenu extends BaseEntity {
    private String id;
    private String name;
    private String path;
}
