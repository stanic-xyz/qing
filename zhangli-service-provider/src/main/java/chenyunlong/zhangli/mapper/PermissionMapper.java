package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("select permission.id as id,permission.name as name,permission.description as description FROM user,user_role,role_permission,permission,role WHERE user.userid=user_role.user_id AND user_role.role_id AND role.role_id and role_permission.role_id=role.role_id and role_permission.permission_id = permission.id and user.username=#{username}")
    List<Permission> getPermissionByUsername(String username);
}
