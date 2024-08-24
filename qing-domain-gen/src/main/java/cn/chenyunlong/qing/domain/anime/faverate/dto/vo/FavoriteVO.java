package cn.chenyunlong.qing.domain.anime.faverate.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.anime.faverate.Favorite;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Long;
import java.lang.String;
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
public class FavoriteVO extends AbstractBaseJpaVo {
    @Schema(
            title = "animeId"
    )
    private Long animeId;

    @Schema(
            title = "username",
            description = "username"
    )
    private String username;

    public FavoriteVO(Favorite source) {
        super();
        this.setId(source.getId());;
        this.setCreatedAt(source.getCreatedAt());;
        this.setUpdatedAt(source.getCreatedAt());;
        this.setVersion(source.getVersion());;
        this.setAnimeId(source.getAnimeId());
        this.setUsername(source.getUsername());
    }
}
