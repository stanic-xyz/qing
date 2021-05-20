package chenyunlong.zhangli.model.vo.page;

import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoUpdateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IndexModel extends BaseModel {

    private List<AnimeInfoMinimalDTO> recommendList;
    private List<AnimeInfoMinimalDTO> recentList;
    private List<AnimeInfoMinimalDTO> dalyUpdateList;
    private List<AnimeInfoUpdateDTO> updateInfoList;
}

