package chenyunlong.zhangli.model.entities;

import com.stan.zhangli.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends BaseEntity {

    private String name;
    private String id;
    private String description;

}
