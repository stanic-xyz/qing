package cn.chenyunlong.qing.domain.auth.role.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.role.Role;

public interface RoleRepository extends BaseRepository<Role, Long> {

    /**
     * 根据角色名称查询角色
     */
    Role findRoleByRole(String role);

    /**
     * 根据角色名称查询角色
     */
    Role findRoleByRoleName(String roleName);
}
