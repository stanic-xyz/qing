package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.entiry;

import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 角色-权限关联JPA实体
 * 用于持久化角色与权限的多对多关系
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sys_role_permission")
public class RolePermissionEntity extends BaseJpaEntity {

    /**
     * 主键ID
     */
    @Column(name = "id")
    private Long id;

    /**
     * 角色ID
     */
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    /**
     * 权限ID
     */
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
}
