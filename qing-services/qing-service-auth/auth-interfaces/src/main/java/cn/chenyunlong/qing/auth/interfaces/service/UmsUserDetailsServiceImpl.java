//package cn.chenyunlong.qing.auth.interfaces.service;
//
//import cn.chenyunlong.qing.auth.domain.user.command.UserRegistrationCommand;
//import cn.chenyunlong.qing.auth.application.service.SecurityPolicyService;
//import cn.chenyunlong.qing.auth.application.service.UserService;
//import cn.chenyunlong.qing.auth.domain.rbac.Role;
//import cn.chenyunlong.qing.auth.domain.user.QingUser;
//import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
//import cn.chenyunlong.qing.infrastructure.email.api.IEmailService;
//import cn.chenyunlong.qing.security.config.SecurityProperties;
//import cn.chenyunlong.qing.security.entity.AuthUser;
//import cn.chenyunlong.qing.security.enums.ErrorCodeEnum;
//import cn.chenyunlong.qing.security.exception.RegisterUserFailureException;
//import cn.chenyunlong.qing.security.service.UmsUserDetailsService;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
/// **
// * 用户详情服务实现类
// * 用于Spring Security认证
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UmsUserDetailsServiceImpl implements UmsUserDetailsService {
//
//    private final UserService userService;
//    private final IEmailService emailService;
//    private final SecurityProperties securityProperties;
//    private final SecurityPolicyService securityPolicyService;
//
//    @Override
//    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
//        QingUser qingUser = userService.loadUserById(userId).orElseThrow(() -> new UsernameNotFoundException("用户名未找到!"));
//        return buildUserDetails(qingUser);
//    }
//
//    @NotNull
//    private UserDetails buildUserDetails(QingUser qingUser) {
//        List<Role> roleList = CollUtil.toList();
//        return User.builder()
//                .username(qingUser.getUsername().value())
//                .password(qingUser.getEncodedPassword().value())
//                .credentialsExpired(false)
//                .accountExpired(false)
//                .authorities(AuthorityUtils.createAuthorityList(roleList.stream().map(Role::getCode).toList()))
//                .disabled(qingUser.getValidStatus().boolValue())
//                .build();
//    }
//
//    @Override
//    public List<Boolean> existedByNickNames(String... nicknames) {
//        List<QingUser> userNames = userService.findByUserNickNames(Arrays.stream(nicknames).map(Username::of).collect(Collectors.toSet()));
//        List<Username> existsUserNames = userNames.stream().map(QingUser::getUsername).toList();
//        return Arrays.stream(nicknames).map(username ->
//                existsUserNames.stream().anyMatch(existsUsername -> StrUtil.equals(username, existsUsername.value()))).collect(Collectors.toList());
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<QingUser> userOptional = Optional.empty();
//        if (StrUtil.contains(username, "@")) {
//            userOptional = userService.findByEmail(username);
//        }
//        if (userOptional.isEmpty()) {
//            userOptional = userService.findByUsername(username);
//            if (userOptional.isEmpty()) {
//                throw new UsernameNotFoundException("用户名未找到!");
//            }
//        }
//        QingUser qingUser = userOptional.get();
//        log.info("Demo ======>: 登录用户名：{}, 查询成功", username);
//        return buildUserDetails(qingUser);
//    }
//
//    @Override
//    public UserDetails registerUser(AuthUser authUser, String nickname, String defaultAuthority, String decodeState) throws RegisterUserFailureException {
//        String defaultPassword = securityPolicyService.getDefaultPassword();
//        UserRegistrationCommand creator = UserRegistrationCommand.create(IdUtil.getSnowflakeNextIdStr(), defaultPassword, authUser.getEmail());
//        emailService.addEmailTaskToList();
//        QingUser register = userService.register(creator);
//        if (register == null) {
//            throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, authUser.getUsername());
//        }
//        return buildUserDetails(register);
//    }
//}
