package cn.chenyunlong.qing.auth.domain.user.valueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 用户角色权限信息值对象
 * 封装用户的所有角色和权限信息，用于登录认证和令牌生成
 *
 * @author 陈云龙
 */
@Getter
@AllArgsConstructor
public class UserRolePermissionInfo {
    
    /**
     * 用户角色编码列表
     */
    private final List<String> roleCodes;
    
    /**
     * 用户权限编码列表
     */
    private final List<String> permissionCodes;
    
    /**
     * 创建用户角色权限信息
     *
     * @param roleCodes 角色编码列表
     * @param permissionCodes 权限编码列表
     * @return UserRolePermissionInfo实例
     */
    public static UserRolePermissionInfo of(List<String> roleCodes, List<String> permissionCodes) {
        return new UserRolePermissionInfo(roleCodes, permissionCodes);
    }
    
    /**
     * 检查用户是否拥有指定角色
     *
     * @param roleCode 角色编码
     * @return 是否拥有该角色
     */
    public boolean hasRole(String roleCode) {
        return roleCodes.contains(roleCode);
    }
    
    /**
     * 检查用户是否拥有指定权限
     *
     * @param permissionCode 权限编码
     * @return 是否拥有该权限
     */
    public boolean hasPermission(String permissionCode) {
        return permissionCodes.contains(permissionCode);
    }
    
    /**
     * 检查用户是否拥有所有指定角色
     *
     * @param requiredRoleCodes 要求的角色编码列表
     * @return 是否拥有所有要求的角色
     */
    public boolean hasAllRoles(List<String> requiredRoleCodes) {
        return roleCodes.containsAll(requiredRoleCodes);
    }
    
    /**
     * 检查用户是否拥有所有指定权限
     *
     * @param requiredPermissionCodes 要求的权限编码列表
     * @return 是否拥有所有要求的权限
     */
    public boolean hasAllPermissions(List<String> requiredPermissionCodes) {
        return permissionCodes.containsAll(requiredPermissionCodes);
    }
}