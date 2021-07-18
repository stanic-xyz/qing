package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends BaseEntity {

    private String name;
    private String id;
    private String description;

}
