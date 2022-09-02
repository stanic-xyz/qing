package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends BaseEntity<Permission> {

    private String name;
    private String id;
    private String description;

}