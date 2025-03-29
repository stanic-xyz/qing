package cn.chenyunlong.qing.domain.auth.connection.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionData {

    /**
     * 本地用户id
     */
    private String userId;
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
