package chenyunlong.zhangli.model.params;

import chenyunlong.zhangli.model.dto.base.InputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddEpisodeParam implements InputConverter<AnimeEpisodeEntity> {
    private Long animeId;
    @NotBlank(message = "视频名称不能为空")
    private String name;
    private String uploaderName;
    private Long uploaderId;
    @NotBlank(message = "播放地址不能为空")
    private String url;
    private Integer orderNo;
}
