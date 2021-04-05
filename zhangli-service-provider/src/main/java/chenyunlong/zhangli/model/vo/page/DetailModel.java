package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailModel extends BaseModel {

    private AnimeInfoVo animeInfo;
    private List<AnimeInfoMinimalDTO> relevant = new LinkedList<>();
    private List<AnimeInfoMinimalDTO> recommendation = new LinkedList<>();
}
