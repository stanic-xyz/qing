package cn.chenyunlong.qing.domain.anime.episode.dto.updater;

import cn.chenyunlong.qing.domain.anime.episode.Episode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class EpisodeUpdater {

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "animeId",
        description = "动漫ID"
    )
    private Long animeId;

    @Schema(
        title = "collectionId",
        description = "播放源ID"
    )
    private Long collectionId;

    @Schema(
        title = "collectionName",
        description = "播放源名称"
    )
    private String collectionName;

    private Long id;

    public void updateEpisode(Episode param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getAnimeId()).ifPresent(param::setAnimeId);
        Optional.ofNullable(getCollectionId()).ifPresent(param::setCollectionId);
        Optional.ofNullable(getCollectionName()).ifPresent(param::setCollectionName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAnimeId() {
        return animeId;
    }

    public void setAnimeId(Long animeId) {
        this.animeId = animeId;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
