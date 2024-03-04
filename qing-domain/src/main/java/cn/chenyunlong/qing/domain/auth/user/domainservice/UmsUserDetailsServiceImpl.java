package cn.chenyunlong.qing.domain.auth.user.domainservice;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.domain.auth.user.service.IUserService;
import cn.chenyunlong.qing.infrastructure.support.email.IEmailService;
import cn.chenyunlong.security.entity.AuthUser;
import cn.chenyunlong.security.exception.RegisterUserFailureException;
import cn.chenyunlong.security.service.UmsUserDetailsService;
import cn.hutool.core.util.IdUtil;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UmsUserDetailsServiceImpl implements UmsUserDetailsService {

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Override
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        Optional<QingUser> userOptional = userService.loadUserByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("用户名未找到!");
        }
        QingUser qingUser = userOptional.get();
        return new User(qingUser.getUsername(), qingUser.getPassword(),
            true,
            true,
            true,
            true,
            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_VISIT, ROLE_USER"));
    }

    @Override
    public List<Boolean> existedByUsernames(String... usernames) {
        return Arrays.stream(usernames).map(username -> false).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<QingUser> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("用户名未找到!");
        }
        QingUser qingUser = userOptional.get();
        log.info("Demo ======>: 登录用户名：{}, 登录成功", username);
        return new User(qingUser.getUsername(), qingUser.getPassword(),
            true,
            true,
            true,
            true,
            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_VISIT, ROLE_USER"));
    }

    @Override
    public UserDetails registerUser(AuthUser authUser, String username, String defaultAuthority, String decodeState) throws RegisterUserFailureException {
        UserCreator creator = new UserCreator();
        creator.setUid(IdUtil.getSnowflakeNextId());
        creator.setUsername(username);
        creator.setPassword("test");
        creator.setEmail(authUser.getEmail());
        creator.setDescription(authUser.getRemark());
        // 默认密码，123456
        String encodedPassword = passwordEncoder.encode("123456");
        creator.setPassword(encodedPassword);
        creator.setAvatar(authUser.getAvatar());
        creator.setMfaType(MFAType.NONE);
        emailService.addEmailTaskToList();
        userService.register(creator);
        return User.builder()
                   .username(String.valueOf(creator.getUid()))
                   .password(encodedPassword)
                   .disabled(false)
                   .accountExpired(false)
                   .accountLocked(false)
                   .credentialsExpired(false)
                   .authorities(Collections.emptyList())
                   .build();
    }
}
