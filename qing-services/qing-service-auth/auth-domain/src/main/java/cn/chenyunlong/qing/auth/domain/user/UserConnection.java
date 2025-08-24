package cn.chenyunlong.qing.auth.domain.user;

import cn.chenyunlong.qing.auth.domain.user.dto.enums.AuthProvider;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * User userConnection extension.
 *
 * @author guqing
 * @since 2.4.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnection {

    /**
     * The name of the OAuth provider (e.g. Google, Facebook, Twitter).
     */
    @Schema(requiredMode = REQUIRED)
    private String registrationId;

    private AuthProvider authProvider;

    @Schema(requiredMode = REQUIRED)
    private String username;

    /**
     * The unique identifier for the user's userConnection to the OAuth provider.
     * for example, the user's GitHub id.
     */
    @Schema(requiredMode = REQUIRED)
    private String providerUserId;

    /**
     * The display name for the user's userConnection to the OAuth provider.
     */
    @Schema(requiredMode = REQUIRED)
    private String displayName;

    /**
     * The URL to the user's profile page on the OAuth provider.
     * For example, the user's GitHub profile URL.
     */
    private String profileUrl;

    /**
     * The URL to the user's avatar image on the OAuth provider.
     * For example, the user's GitHub avatar URL.
     */
    private String avatarUrl;

    /**
     * The access token provided by the OAuth provider.
     */
    @Schema(requiredMode = REQUIRED)
    private String accessToken;

    /**
     * The refresh token provided by the OAuth provider (if applicable).
     */
    private String refreshToken;

    private Instant expiresAt;

    private Instant updatedAt;

    boolean equals(UserConnection userConnection) {
        if (getAuthProvider() != userConnection.getAuthProvider()) {
            return false;
        }
        return StrUtil.equals(getUsername(), userConnection.getUsername());
    }
}
