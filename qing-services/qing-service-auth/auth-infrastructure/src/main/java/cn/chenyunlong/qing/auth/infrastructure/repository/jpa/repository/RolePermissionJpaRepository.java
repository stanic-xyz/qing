package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RolePermissionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色-权限关联JPA仓储
 */
@Repository
public interface RolePermissionJpaRepository extends BaseJpaRepository<RolePermissionEntity, Long> {

    /**
     * 判断角色是否已关联指定权限
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);

    /**
     * 根据角色ID查询已关联的权限记录
     */
    List<RolePermissionEntity> findByRoleId(Long roleId);

    /**
     * 取消角色与权限的关联
     */
    void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
