package cn.chenyunlong.qing.application.manager.service.auth;

import cn.chenyunlong.common.enums.MFAType;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UmsUserDetailsServiceImpl implements UmsUserDetailsService {

    private final IUserService userService;
    private final IEmailService emailService;
    private final SecurityProperties securityProperties;

    @Override
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        Optional<QingUser> userOptional = userService.loadUserByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("用户名未找到!");
        }
        QingUser qingUser = userOptional.get();
        return User.builder()
                   .username(String.valueOf(qingUser.getUid()))
                   .password(qingUser.getPassword())
                   .credentialsExpired(false)
                   .accountExpired(false)
                   .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_VISIT, ROLE_USER"))
                   .disabled(false)
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
        log.info("Demo ======>: 登录用户名：{}, 登录成功", username);
        return User.builder()
                   .username(qingUser.getUsername())
                   .password(qingUser.getPassword())
                   .credentialsExpired(false)
                   .accountExpired(false)
                   .accountLocked(false)
                   .accountExpired(false)
                   .disabled(false)
                   .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_VISIT, ROLE_USER"))
                   .build();
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
        return User.builder()
                   .username(String.valueOf(register.getUid()))
                   .password(register.getPassword())
                   .disabled(false)
                   .accountExpired(false)
                   .accountLocked(false)
                   .credentialsExpired(false)
                   .authorities(Collections.emptyList())
                   .build();
    }
}
