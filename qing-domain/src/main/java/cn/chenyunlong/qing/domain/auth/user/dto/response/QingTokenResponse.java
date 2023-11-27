package cn.chenyunlong.qing.domain.auth.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QingTokenResponse {
    /**
     * 访问令牌
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 身份令牌
     */
    @JsonProperty("id_token")
    private String idToken;

    /**
     * 过期时间
     */
    @JsonProperty("expires_in")
    private Number expiresIn;

    /**
     * 刷新令牌
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 令牌类型
     */
    @JsonProperty("token_type")
    private String tokenType;

}
