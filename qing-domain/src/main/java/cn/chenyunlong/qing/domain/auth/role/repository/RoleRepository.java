package cn.chenyunlong.qing.domain.auth.role.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.role.Role;

import java.util.List;

public interface RoleRepository extends BaseRepository<Role, Long> {

    /**
     * 根据角色名称查询角色
     */
    Role findRoleByRole(String role);

    /**
     * 根据角色名称查询角色
     */
    Role findRoleByRoleName(String roleName);

    /**
     * 根据用户ID查询角色
     */
    List<Role> findRolesByUserId(Long userId);
}
