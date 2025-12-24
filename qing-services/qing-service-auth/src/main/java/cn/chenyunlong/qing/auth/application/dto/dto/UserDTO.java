package cn.chenyunlong.qing.auth.application.dto.dto;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * 用户信息
 *
 * @author 陈云龙
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDTO {

    @FieldDesc(name = "用户唯一ID")
    private Long uid;

    @FieldDesc(name = "用户名", description = "用户（唯一），用于前端显示！")
    private String username;

    @FieldDesc(name = "昵称", description = "用户名称（唯一），用于前端显示！")
    private String nickname;

    @FieldDesc(name = "密码过期")
    private Boolean credentialsExpiredAt;

    private boolean active;

    private boolean locked;

    private String phone;
    private String email;
    private String avatar;
    private String description;

    private Instant expireTime;

    private String mfaKey;

    private List<String> roles;

    private List<String> permissions;

    public static UserDTO from(User user) {

        List<UserRole> userRoleList = user.getRoles();
        List<String> permissionCodes = userRoleList.stream().flatMap(userRole -> userRole.getPermissions().stream()).map(Permission::getCode).toList();

        return UserDTO.builder()
                .phone(user.getPhone().normalized())
                .username(user.getUsername().value())
                .nickname(user.getNickname())
                .active(user.isActive())
                .locked(user.isLocked())
                .expireTime(user.getExpireTime())
                .uid(user.getUid()).username(user.getUsername().value())
                .avatar(user.getAvatar())
                .email(user.getEmail().value())
                .description(user.getDescription())
                .expireTime(user.getExpireTime())
                .roles(userRoleList.stream().map(UserRole::getRole).map(Role::getCode).collect(toList()))
                .permissions(permissionCodes)
                .build();
    }
}
