package chenyunlong.zhangli.model.vo;

import chenyunlong.zhangli.model.dto.EpisodeDTO;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class PlayListVO extends PlayListDTO {
    private List<EpisodeDTO> playList;
}
