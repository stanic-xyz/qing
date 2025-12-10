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

package cn.chenyunlong.qing.auth.domain.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A data transfer object that allows the internal state of a Connection to be persisted and transferred between layers of an application.
 * Some fields may be null .
 * For example, an OAuth2Connection has a null 'secret' field while an OAuth1Connection has null 'refreshToken' and 'expireTime' fields.
 *
 * @author Keith Donald
 * @author YongWu zheng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionData implements Serializable {

    /**
     * 本地用户id
     */
    private Long userId;
    /**
     * 第三方服务商
     */
    private String providerId;
    /**
     * 第三方用户id
     */
    private String providerUserId;

    /**
     * userId 绑定同一个 providerId 的排序
     */
    private Integer rank;
    /**
     * 第三方用户名
     */
    private String displayName;
    /**
     * 主页
     */
    private String profileUrl;
    /**
     * 头像
     */
    private String imageUrl;
    /**
     * accessToken
     */
    private String accessToken;
    /**
     * auth_token.id
     */
    private Long tokenId;
    /**
     * refreshToken
     */
    private String refreshToken;

    /**
     * 过期日期, 基于 1970-01-01T00:00:00Z, 无过期时间默认为 -1
     */
    private Long expireTime;

}
