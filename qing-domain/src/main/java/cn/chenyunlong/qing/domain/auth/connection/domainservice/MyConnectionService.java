package cn.chenyunlong.qing.domain.auth.connection.domainservice;

import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.mapper.UserConnectionMapper;
import cn.chenyunlong.qing.domain.auth.connection.repository.UserConnectionRepository;
import cn.chenyunlong.qing.domain.auth.connection.service.IUserConnectionService;
import cn.chenyunlong.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.security.entity.AuthToken;
import cn.chenyunlong.security.entity.AuthUser;
import cn.chenyunlong.security.entity.ConnectionData;
import cn.chenyunlong.security.enums.ErrorCodeEnum;
import cn.chenyunlong.security.exception.RegisterUserFailureException;
import cn.chenyunlong.security.exception.UpdateConnectionException;
import cn.chenyunlong.security.service.UmsUserDetailsService;
import cn.chenyunlong.security.signup.ConnectionService;
import cn.hutool.json.JSONUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyConnectionService implements ConnectionService {

    private final UmsUserDetailsService userDetailsService;
    private final AuthingProperties authingProperties;
    private final IUserConnectionService userConnectionService;
    private final UserConnectionRepository connectionRepository;


    @Override
    public UserDetails signUp(AuthUser authUser, String providerId, String encodeState)
        throws RegisterUserFailureException {
        String username = authUser.getUsername();
        String[] usernames = userDetailsService.generateUsernames(authUser);
        try {
            // 重名检查
            username = null;
            final List<Boolean> existedByUserIds = userDetailsService.existedByUsernames(usernames);
            for (int i = 0, len = existedByUserIds.size(); i < len; i++) {
                if (!existedByUserIds.get(i)) {
                    username = usernames[i];
                    break;
                }
            }
            // 用户重名, 自动注册失败
            if (username == null) {
                throw new RegisterUserFailureException(ErrorCodeEnum.USERNAME_USED,
                    authUser.getUsername());
            }
            // 解密 encodeState  https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7
            // 注册到本地账户
            UserDetails userDetails = userDetailsService.registerUser(authUser, username,
                authingProperties.getDefaultAuthorities(), encodeState);
            // 第三方授权登录信息绑定到本地账号, 且添加第三方授权登录信息到 user_connection 与 auth_token
            registerConnection(providerId, authUser, userDetails);
            return userDetails;
        } catch (Exception exception) {
            log.error(String.format("OAuth2自动注册失败: error=%s, username=%s, authUser=%s",
                exception.getMessage(), username, JSONUtil.toJsonPrettyStr(authUser)), exception);
            throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, username);
        }
    }

    /**
     * 绑定用户
     *
     * @param providerId 用户提供者
     * @param authUser 用户信息
     * @param userDetails 用户详情
     */
    private void registerConnection(String providerId, AuthUser authUser, UserDetails userDetails) {
        AuthToken token = authUser.getToken();
        UserConnectionCreator creator = UserConnectionCreator.builder()
            .accessToken(authUser.getToken().getAccessToken())
            .providerId(providerId)
            .displayName(authUser.getUsername())
            .rank(1)
            .imageUrl(authUser.getAvatar())
            .refreshToken(authUser.getToken().getRefreshToken())
            .expireTime(authUser.getToken().getExpireIn())
            .userId(userDetails.getUsername())
            .providerUserId(authUser.getUuid())
            .build();
        Long userConnection = userConnectionService.createUserConnection(creator);
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
    public List<ConnectionData> findConnectionByProviderIdAndProviderUserId(String providerId,
        String providerUserId) {
        return connectionRepository.findConnectionByProviderIdAndProviderUserId(providerId,
                providerUserId)
            .stream()
            .map(UserConnectionMapper.INSTANCE::entityToConnectionData)
            .collect(Collectors.toList());
    }

    @Override
    public MultiValueMap<String, ConnectionData> listAllConnections(String userId) {
        return null;
    }
}
