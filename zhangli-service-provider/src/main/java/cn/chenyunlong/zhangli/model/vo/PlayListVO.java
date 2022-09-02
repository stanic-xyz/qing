package cn.chenyunlong.zhangli.model.vo;

import cn.chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.zhangli.model.dto.PlayListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class PlayListVO extends PlayListDTO {
    private List<AnimeEpisodeDTO> playList;
}
