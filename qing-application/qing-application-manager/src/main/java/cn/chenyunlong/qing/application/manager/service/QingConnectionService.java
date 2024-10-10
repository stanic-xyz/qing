package cn.chenyunlong.qing.application.manager.service;

import cn.chenyunlong.qing.domain.auth.connection.dto.creator.UserConnectionCreator;
import cn.chenyunlong.qing.domain.auth.connection.dto.response.UserConnectionData;
import cn.chenyunlong.qing.domain.auth.connection.mapper.UserConnectionMapper;
import cn.chenyunlong.qing.domain.auth.connection.repository.UserConnectionRepository;
import cn.chenyunlong.qing.domain.auth.connection.service.IUserConnectionService;
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
    private final IUserConnectionService userConnectionService;
    private final UserConnectionRepository connectionRepository;


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
            registerConnection(authUser, userDetails);
            return userDetails;
        } catch (Exception exception) {
            log.error(String.format("OAuth2自动注册失败: error=%s, nickName=%s, authUser=%s", exception.getMessage(), nickName, JSONUtil.toJsonPrettyStr(authUser)), exception);
            throw new RegisterUserFailureException(ErrorCodeEnum.USER_REGISTER_FAILURE, nickName);
        }
    }

    /**
     * 绑定用户
     *
     * @param authUser    用户信息
     * @param userDetails 用户详情
     */
    private void registerConnection(AuthUser authUser, UserDetails userDetails) {
        UserConnectionCreator creator = UserConnectionCreator.builder()
            .accessToken(authUser.getToken().getAccessToken())
            .providerId(authUser.getSource().getProviderId())
            .displayName(authUser.getUsername())
            .rank(1)
            .imageUrl(authUser.getAvatar())
            .refreshToken(authUser.getToken().getRefreshToken())
            .expireTime(authUser.getToken().getExpireIn())
            .userId(userDetails.getUsername())
            .providerUserId(authUser.getUuid())
            .build();
        userConnectionService.createUserConnection(creator);
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
        return connectionRepository.findConnectionByProviderIdAndProviderUserId(provider.getProviderId(),
                providerUserId)
            .stream()
            .map(connection -> {
                UserConnectionData connectionData = UserConnectionMapper.INSTANCE.entityToConnectionData(connection);
                return BeanUtil.copyProperties(connectionData, ConnectionData.class);
            })
            .collect(Collectors.toList());
    }

    @Override
    public MultiValueMap<String, ConnectionData> listAllConnections(String userId) {
        return null;
    }
}
