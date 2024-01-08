package cn.chenyunlong.qing.domain.anime.episode.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
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
public class EpisodeVO extends AbstractBaseJpaVo {
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

    public EpisodeVO(Episode source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setAnimeId(source.getAnimeId());
        this.setCollectionId(source.getCollectionId());
        this.setCollectionName(source.getCollectionName());
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
}
