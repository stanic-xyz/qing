package cn.chenyunlong.qing.domain.anime.faverate.dto.updater;

import cn.chenyunlong.qing.domain.anime.faverate.Favorite;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Long;
import java.lang.String;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class FavoriteUpdater {
    @Schema(
            title = "animeId"
    )
    private Long animeId;

    @Schema(
            title = "username",
            description = "username"
    )
    private String username;

    private Long id;

    public void updateFavorite(Favorite param) {
        Optional.ofNullable(getAnimeId()).ifPresent(param::setAnimeId);
        Optional.ofNullable(getUsername()).ifPresent(param::setUsername);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
