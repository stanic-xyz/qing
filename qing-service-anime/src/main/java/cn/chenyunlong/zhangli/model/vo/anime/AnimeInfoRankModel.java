package cn.chenyunlong.zhangli.model.vo.anime;

import cn.chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import lombok.*;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class AnimeInfoRankModel {

    private List<AnimeInfo> animeInfoList;
    private long totalCount;

    public AnimeInfoRankModel(List<AnimeInfo> animeInfoList, long totalCount) {
        this.animeInfoList = animeInfoList;
        this.totalCount = totalCount;
    }

}
