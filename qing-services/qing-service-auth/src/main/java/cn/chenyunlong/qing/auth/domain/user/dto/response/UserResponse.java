package cn.chenyunlong.qing.auth.domain.user.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.auth.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Setter
@Getter
@Schema
@Builder(access = AccessLevel.PRIVATE)
public class UserResponse extends AbstractJpaResponse {


    private Long id;
    private String username;

    // UTC时间
    private Instant registeredAt;

    private Instant lastLoginAt;

    // 本地时间
    private LocalDateTime localRegisteredAt;

    private LocalDateTime localLastLoginAt;

    private String timeZone;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId().id())
                .username(user.getUsername().value())
                .registeredAt(user.getRegisteredAt())
                .timeZone(ZoneOffset.UTC.getId())
                .build();
    }
}
