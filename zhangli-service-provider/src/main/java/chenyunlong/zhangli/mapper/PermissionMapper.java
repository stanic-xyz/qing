package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 */
@Mapper
@Component
public interface PermissionMapper {
    /**
     * 获取用户的权限信息
     *
     * @param username 用户名称
     * @return 遍历
     */
    @Select("SELECT DISTINCT permission.name AS NAME FROM USER,user_role,role_permission,permission,role WHERE user.userid=user_role.user_id AND user_role.role_id AND role.role_id AND role_permission.role_id=role.role_id AND role_permission.permission_id = permission.id AND user.username=#{username}")
    List<Permission> getPermissionByUsername(String username);

}
