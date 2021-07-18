package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import lombok.*;

/**
 * @author Stan
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class District extends BaseEntity {
    private Long id;
    private String name;
    private String code;
    private String description;
}
