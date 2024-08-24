package cn.chenyunlong.qing.domain.anime.playlist.dto.updater;

import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class PlayListUpdater {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "description"
    )
    private String description;

    @Schema(
        title = "icon"
    )
    private String icon;

    private Long id;

    public void updatePlayList(PlayList param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getIcon()).ifPresent(param::setIcon);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
