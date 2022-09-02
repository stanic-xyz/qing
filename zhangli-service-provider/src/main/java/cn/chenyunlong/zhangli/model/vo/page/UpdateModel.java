package cn.chenyunlong.zhangli.model.vo.page;

import cn.chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateModel extends BaseModel {
    private List<AnimeInfoMinimalDTO> animeList;
}
