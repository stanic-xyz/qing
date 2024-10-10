package cn.chenyunlong.qing.domain.auth.connection.dto.updater;

import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class UserConnectionUpdater {

    @Schema(
        title = "userId",
        description = "userId"
    )
    private String userId;

    @Schema(
        title = "providerId",
        description = "providerId"
    )
    private String providerId;

    @Schema(
        title = "providerUserId",
        description = "providerUserId"
    )
    private String providerUserId;

    @Schema(
        title = "rank",
        description = "rank"
    )
    private Integer rank;

    @Schema(
        title = "displayName",
        description = "displayName"
    )
    private String displayName;

    @Schema(
        title = "profileUrl",
        description = "profileUrl"
    )
    private String profileUrl;

    @Schema(
        title = "imageUrl",
        description = "imageUrl"
    )
    private String imageUrl;

    @Schema(
        title = "accessToken",
        description = "accessToken"
    )
    private String accessToken;

    @Schema(
        title = "tokenId",
        description = "tokenId"
    )
    private Long tokenId;

    @Schema(
        title = "refreshToken",
        description = "refreshToken"
    )
    private String refreshToken;

    @Schema(
        title = "expireTime",
        description = "expireTime"
    )
    private Long expireTime;

    private Long id;

    public void updateUserConnection(UserConnection param) {
        Optional.ofNullable(getUserId()).ifPresent(param::setUserId);
        Optional.ofNullable(getProviderId()).ifPresent(param::setProviderId);
        Optional.ofNullable(getProviderUserId()).ifPresent(param::setProviderUserId);
        Optional.ofNullable(getRank()).ifPresent(param::setRank);
        Optional.ofNullable(getDisplayName()).ifPresent(param::setDisplayName);
        Optional.ofNullable(getProfileUrl()).ifPresent(param::setProfileUrl);
        Optional.ofNullable(getImageUrl()).ifPresent(param::setImageUrl);
        Optional.ofNullable(getAccessToken()).ifPresent(param::setAccessToken);
        Optional.ofNullable(getTokenId()).ifPresent(param::setTokenId);
        Optional.ofNullable(getRefreshToken()).ifPresent(param::setRefreshToken);
        Optional.ofNullable(getExpireTime()).ifPresent(param::setExpireTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
