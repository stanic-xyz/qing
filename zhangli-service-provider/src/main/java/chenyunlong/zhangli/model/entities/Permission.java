package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;

@Data
public class Permission extends BaseEntity {

    private String name;
    private String id;
    private String description;

}
