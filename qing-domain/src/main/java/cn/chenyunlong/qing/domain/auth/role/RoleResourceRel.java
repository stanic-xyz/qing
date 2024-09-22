package cn.chenyunlong.qing.domain.auth.role;

import cn.chenyunlong.codegen.annotation.GenRepository;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@GenRepository
@Entity
@Table(name = "sys_role_resource_rel")
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleResourceRel extends BaseJpaAggregate {

    private Long roleId;

    private Long resourceId;

}
