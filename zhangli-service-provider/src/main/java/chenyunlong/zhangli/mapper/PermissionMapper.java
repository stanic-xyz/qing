package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {
    @Select("SELECT DISTINCT permission.name AS NAME FROM USER,user_role,role_permission,permission,role WHERE user.userid=user_role.user_id AND user_role.role_id AND role.role_id AND role_permission.role_id=role.role_id AND role_permission.permission_id = permission.id AND user.username=#{username}")
    List<Permission> getPermissionByUsername(String username);

}