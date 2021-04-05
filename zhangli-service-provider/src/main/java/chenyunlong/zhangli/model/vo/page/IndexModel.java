package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class IndexModel extends BaseModel {

    private List<AnimeInfoMinimalDTO> recommendList;
    private List<AnimeInfoMinimalDTO> recentList;
    private List<AnimeInfoMinimalDTO> dalyUpdateList;
}

