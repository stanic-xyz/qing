package cn.chenyunlong.zhangli.model.vo.page;

import cn.chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import cn.chenyunlong.zhangli.model.dto.anime.AnimeInfoUpdateDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class IndexModel extends BaseModel {

    private List<AnimeInfoMinimalDTO> recommendList;
    private List<AnimeInfoMinimalDTO> recentList;
    private Map<Integer, List<AnimeInfoMinimalDTO>> recentMap;
    private List<AnimeInfoMinimalDTO> dalyUpdateList;
    private List<AnimeInfoUpdateDTO> updateInfoList;
}

