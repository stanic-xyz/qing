package cn.chenyunlong.zhangli.model.vo.page;

import cn.chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import cn.chenyunlong.zhangli.model.params.AnimeInfoQuery;
import cn.chenyunlong.zhangli.model.vo.OptionsModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchModel extends BaseModel {
    private AnimeInfoQuery query;
    private List<AnimeInfo> animeInfos;
    private OptionsModel options;
    private Long totalCount;
    private Long currentIndex;
    private Long totalPage;
}
