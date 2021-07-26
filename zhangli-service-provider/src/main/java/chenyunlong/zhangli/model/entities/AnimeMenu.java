package chenyunlong.zhangli.model.entities;

import com.stan.zhangli.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AnimeMenu extends BaseEntity {
    private String id;
    private String name;
    private String path;
}
