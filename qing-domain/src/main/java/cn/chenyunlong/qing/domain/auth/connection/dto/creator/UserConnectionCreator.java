package cn.chenyunlong.qing.domain.auth.connection.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConnectionCreator {

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
}
