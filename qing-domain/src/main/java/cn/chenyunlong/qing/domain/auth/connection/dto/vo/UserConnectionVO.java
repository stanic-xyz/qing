package cn.chenyunlong.qing.domain.auth.connection.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
        callSuper = true
)
@NoArgsConstructor(
        access = AccessLevel.PROTECTED
)
public class UserConnectionVO extends AbstractBaseJpaVo {
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

    public UserConnectionVO(UserConnection source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setVersion(source.getVersion());
        ;
        this.setUserId(source.getUserId());
        this.setProviderId(source.getProviderId());
        this.setProviderUserId(source.getProviderUserId());
        this.setRank(source.getRank());
        this.setDisplayName(source.getDisplayName());
        this.setProfileUrl(source.getProfileUrl());
        this.setImageUrl(source.getImageUrl());
        this.setAccessToken(source.getAccessToken());
        this.setTokenId(source.getTokenId());
        this.setRefreshToken(source.getRefreshToken());
        this.setExpireTime(source.getExpireTime());
    }
}
