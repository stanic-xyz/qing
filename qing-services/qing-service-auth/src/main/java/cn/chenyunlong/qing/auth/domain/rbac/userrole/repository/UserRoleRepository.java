package cn.chenyunlong.qing.auth.domain.rbac.userrole.repository;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends BaseRepository<UserRole, UserRoleId> {

    boolean existsByUserIdAndRoleId(UserId id, RoleId roleNotFoundId);

    Optional<UserRole> findByUserIdAndRoleId(UserId userId, RoleId roleId);
    
    /**
     * 根据用户ID查找所有用户角色关联
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<UserRole> findByUserId(UserId userId);
    
    /**
     * 检查用户是否拥有指定角色
     * @param userId 用户ID
     * @param roleCode 角色编码
     * @return 是否拥有该角色
     */
    boolean userHasRole(UserId userId, String roleCode);
    
    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否拥有该权限
     */
    boolean userHasPermission(UserId userId, String permissionCode);
}
