/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.chenyunlong.qing.security.signup;

import cn.chenyunlong.qing.security.entity.AuthUser;
import cn.chenyunlong.qing.security.entity.ConnectionData;
import cn.chenyunlong.qing.security.enums.AuthProvider;
import cn.chenyunlong.qing.security.exception.RegisterUserFailureException;
import cn.chenyunlong.qing.security.exception.UpdateConnectionException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;

import java.util.List;


/**
 * 在无法从 {@link AuthUser} 映射用户 ID 的情况下注册新用户的命令。允许在提供程序登录尝试期间从连接数据隐式创建本地用户配置文件。
 * 注意：要替换内置的 {@code auth_token} 和 {@code user_connection} 表的实现逻辑，
 * 实现此接口注入 IOC 容器，并设置属性 ums.repository.enableStartUpInitializeTable = false。
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020-10-08 20:10
 */
public interface ConnectionService {

    /**
     * Sign up a new user of the application from the connection.
     * 如果 {@code authUser.getUsername()} 重名, 则使用 {@code authUser.getUsername() + "_" + authUser.getSource()} 或
     * {@code authUser.getUsername() + "_" + authUser.getSource() +  "_" + authUser.getUuid()} 即
     * username_{providerId}_{providerUserId}.
     *
     * @param authUser    the user info from the provider sign-in attempt
     * @param encodeState 加密后的 state.   {@code https://gitee.com/pcore/just-auth-spring-security-starter/issues/I22JC7}
     * @return the new user UserDetails.
     * @throws RegisterUserFailureException 用户重名或注册失败
     */
    UserDetails signUp(AuthUser authUser, String encodeState) throws RegisterUserFailureException;

    /**
     * 根据传入的参数更新第三方授权登录的用户信息, 包括 accessToken 信息,
     *
     * @param authUser       {@link AuthUser}
     * @param connectionData 第三方授权登录的用户信息
     * @throws UpdateConnectionException 更新异常
     */
    void updateUserConnectionAndAuthToken(final AuthUser authUser, final ConnectionData connectionData) throws UpdateConnectionException;

    /**
     * 第三方授权登录信息{@link AuthUser}绑定到本地账号{@link UserDetails}, 且添加第三方授权登录信息到 user_connection 与 auth_token
     *
     * @param principal  本地用户数据
     * @param authUser   第三方用户信息
     * @param providerId 第三方服务商 Id
     */
    void binding(UserDetails principal, AuthUser authUser, String providerId);

    /**
     * 解除绑定(第三方)
     *
     * @param userId         用户 Id
     * @param providerId     第三方服务商 Id
     * @param providerUserId 第三方用户 Id
     */
    void unbinding(String userId, String providerId, String providerUserId);

    /**
     * 根据 providerId 与 providerUserId 获取 ConnectionData list.
     *
     * @param provider       第三方服务商, 如: qq, github
     * @param providerUserId 第三方用户 Id
     * @return connection data list
     */
    @Nullable
    List<ConnectionData> findConnectionByProviderIdAndProviderUserId(AuthProvider provider, String providerUserId);

    /**
     * 获取当前账号下所有绑定的第三方账号接口.<br>
     * Find all connections the current user has across all providers.
     * The returned map contains an entry for each provider the user is connected to.
     * The key for each entry is the providerId, and the value is the list of {@link ConnectionData}s that exist between the user and that provider.
     * For example, if the user is connected once to Facebook and twice to Twitter, the returned map would contain two entries with the following structure:
     * <pre>
     * {
     *     "qq" -&gt; Connection("Jack") ,
     *     "github"  -&gt; Connection("Tomas"), Connection("Jessica")
     * }
     * </pre>
     * The returned map is sorted by providerId and entry values are ordered by rank.
     * Returns an empty map if the user has no connections.
     *
     * @param userId the userId
     * @return all connections the current user has across all providers.
     */

    MultiValueMap<String, ConnectionData> listAllConnections(String userId);
}
