package cn.chenyunlong.qing.domain.auth.user;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User connection extension.
 *
 * @author guqing
 * @since 2.4.0
 */
@Data
@Table(name = "sys_user_connection")
@EqualsAndHashCode(callSuper = true)
public class UserConnection extends BaseJpaAggregate {


    /**
     * The name of the OAuth provider (e.g. Google, Facebook, Twitter).
     */
    @Schema(requiredMode = REQUIRED)
    private String registrationId;

    @Schema(requiredMode = REQUIRED)
    private String username;

    /**
     * The unique identifier for the user's connection to the OAuth provider.
     * for example, the user's GitHub id.
     */
    @Schema(requiredMode = REQUIRED)
    private String providerUserId;

    /**
     * The display name for the user's connection to the OAuth provider.
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
}
