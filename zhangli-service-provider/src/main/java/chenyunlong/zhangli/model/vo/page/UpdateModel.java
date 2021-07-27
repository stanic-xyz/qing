package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateModel extends BaseModel {
    private List<AnimeInfoMinimalDTO> animeList;
}
