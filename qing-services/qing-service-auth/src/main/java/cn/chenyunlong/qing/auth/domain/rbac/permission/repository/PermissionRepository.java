package cn.chenyunlong.qing.auth.domain.rbac.permission.repository;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

/**
 * 权限仓储接口，提供权限的持久化访问能力
 */
public interface PermissionRepository extends BaseRepository<Permission, PermissionId> {

    /**
     * 根据编码判断权限是否存在
     *
     * @param code 权限编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 根据名称判断权限是否存在
     *
     * @param name 权限名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据多个ID查询权限集合
     *
     * @param permissionIds 权限ID列表
     * @return 权限列表
     */
    List<Permission> findByIds(List<Long> permissionIds);
}
