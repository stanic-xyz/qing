package cn.chenyunlong.qing.auth.interfaces.service;

import cn.chenyunlong.qing.auth.application.service.UserService;
import cn.chenyunlong.qing.auth.domain.user.QingConnectionData;
import cn.chenyunlong.qing.auth.domain.user.dto.entity.QingAuthUser;
import cn.chenyunlong.qing.auth.infrastructure.converter.UserConnectionMapper;
import cn.chenyunlong.qing.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.qing.security.entity.AuthUser;
import cn.chenyunlong.qing.security.entity.ConnectionData;
import cn.chenyunlong.qing.security.enums.AuthProvider;
import cn.chenyunlong.qing.security.enums.ErrorCodeEnum;
import cn.chenyunlong.qing.security.exception.RegisterUserFailureException;
import cn.chenyunlong.qing.security.exception.UpdateConnectionException;
import cn.chenyunlong.qing.security.service.UmsUserDetailsService;
import cn.chenyunlong.qing.security.signup.ConnectionService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QingConnectionService implements ConnectionService {

    private final UmsUserDetailsService userDetailsService;
    private final AuthingProperties authingProperties;
    private final UserService userService;


    @Override
    public UserDetails signUp(AuthUser authUser, String encodeState) throws RegisterUserFailureException {
        String nickName = authUser.getNickname();
        String[] usernames = userDetailsService.generateUserNickNames(authUser);
        try {
            // 重名检查
            final List<Boolean> existedByUserIds = userDetailsService.existedByNickNames(usernames);
            for (int i = 0, len = existedByUserIds.size(); i < len; i++) {
                if (!existedByUserIds.get(i)) {
                    nickName = usernames[i];
                    break;
                }
            }
            // 用户重名, 自动注册失败
            if (nickName == null) {
                // 生成一个随机用户名
                nickName = "用户" + IdUtil.simpleUUID().toUpperCase();
            }
            // 解密 encodeState  https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7
            // 注册到本地账户
            UserDetails userDetails = userDetailsService.registerUser(authUser, nickName, authingProperties.getDefaultAuthorities(), encodeState);
            // 第三方授权登录信息绑定到本地账号, 且添加第三方授权登录信息到 user_connection 与 auth_token

            // 修改为用户信息
            QingAuthUser qingAuthUser = buildQingAuthUser(authUser);

            userService.registerConnection(userDetails.getUsername(), qingAuthUser);
            return userDetails;
        } catch (Exception exception) {
            log.error(String.format("OAuth2自动注册失败: error=%s, nickName=%s, authUser=%s", exception.getMessage(), nickName, JSONUtil.toJsonPrettyStr(authUser)), exception);
            throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, nickName);
        }
    }

    private QingAuthUser buildQingAuthUser(AuthUser authUser) {
        return BeanUtil.copyProperties(authUser, QingAuthUser.class);
    }

    @Override
    public void updateUserConnectionAndAuthToken(AuthUser authUser, ConnectionData connectionData)
        throws UpdateConnectionException {

    }

    @Override
    public void binding(UserDetails principal, AuthUser authUser, String providerId) {

    }

    @Override
    public void unbinding(String userId, String providerId, String providerUserId) {

    }

    @Override
    public List<ConnectionData> findConnectionByProviderIdAndProviderUserId(AuthProvider provider,
                                                                            String providerUserId) {
        return userService.findConnectionByProviderIdAndProviderUserId(provider.getProviderId(),
                providerUserId)
            .stream()
            .map(connection -> {
                QingConnectionData qingConnectionData = UserConnectionMapper.INSTANCE.entityToConnectionData(connection);
                return BeanUtil.copyProperties(qingConnectionData, ConnectionData.class);
            })
            .collect(Collectors.toList());
    }

    @Override
    public MultiValueMap<String, ConnectionData> listAllConnections(String userId) {
        return null;
    }
}
