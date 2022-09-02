package cn.chenyunlong.zhangli.model.vo.page;

import cn.chenyunlong.zhangli.model.params.AnimeInfoQuery;
import cn.chenyunlong.zhangli.model.vo.OptionsModel;
import cn.chenyunlong.zhangli.model.vo.YearInfo;
import cn.chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CatalogModel extends BaseModel {
    private AnimeInfoQuery query;
    private List<AnimeInfoVo> animeList;
    private OptionsModel      options;
    private Long              total;
    private List<YearInfo>    years;
}