package cn.chenyunlong.qing.application.manager.service.auth;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.qing.domain.auth.role.Role;
import cn.chenyunlong.qing.domain.auth.role.service.IRoleService;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import cn.chenyunlong.qing.infrastructure.support.email.IEmailService;
import cn.chenyunlong.qing.security.config.SecurityProperties;
import cn.chenyunlong.qing.security.entity.AuthUser;
import cn.chenyunlong.qing.security.enums.ErrorCodeEnum;
import cn.chenyunlong.qing.security.exception.RegisterUserFailureException;
import cn.chenyunlong.qing.security.service.UmsUserDetailsService;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UmsUserDetailsServiceImpl implements UmsUserDetailsService {

    private final IUserService userService;
    private final IEmailService emailService;
    private final SecurityProperties securityProperties;
    private final IRoleService roleService;

    @Override
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        QingUser qingUser = userService.loadUserById(userId).orElseThrow(() -> new UsernameNotFoundException("用户名未找到!"));
        return buildUserDetails(qingUser);
    }

    @NotNull
    private UserDetails buildUserDetails(QingUser qingUser) {
        List<Role> roleList = roleService.listRoleByUser(qingUser.getId());

        return User.builder()
            .username(qingUser.getUsername())
            .password(qingUser.getPassword())
            .credentialsExpired(false)
            .accountExpired(false)
            .authorities(AuthorityUtils.createAuthorityList(roleList.stream().map(Role::getRole).toList()))
            .disabled(qingUser.getValidStatus().boolValue())
            .build();
    }

    @Override
    public List<Boolean> existedByNickNames(String... nicknames) {
        List<QingUser> userNames = userService.findByUserNickNames(Arrays.stream(nicknames).collect(Collectors.toSet()));
        List<String> existsUserNames = userNames.stream().map(QingUser::getUsername).toList();
        return Arrays.stream(nicknames).map(existsUserNames::contains).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<QingUser> userOptional = Optional.empty();
        if (StrUtil.contains(username, "@")) {
            userOptional = userService.findByEmail(username);
        }
        if (userOptional.isEmpty()) {
            userOptional = userService.findByUsername(username);
            if (userOptional.isEmpty()) {
                throw new UsernameNotFoundException("用户名未找到!");
            }
        }
        QingUser qingUser = userOptional.get();
        log.info("Demo ======>: 登录用户名：{}, 查询成功", username);
        return buildUserDetails(qingUser);
    }

    @Override
    public UserDetails registerUser(AuthUser authUser, String nickname, String defaultAuthority, String decodeState) throws RegisterUserFailureException {
        UserCreator creator = new UserCreator();
        creator.setUsername(IdUtil.getSnowflakeNextIdStr());
        creator.setNickname(nickname);
        creator.setEmail(authUser.getEmail());
        creator.setDescription(authUser.getRemark());
        // 默认密码，123456

        creator.setPassword(securityProperties.getDefaultPassword());
        creator.setAvatar(authUser.getAvatar());
        creator.setMfaType(MFAType.NONE);
        emailService.addEmailTaskToList();
        QingUser register = userService.register(creator).orElseThrow(() -> new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, authUser.getUsername()));
        return buildUserDetails(register);
    }
}
