package chenyunlong.zhangli.model.response.anime;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Stan
 */
@Data
public class AnimeInfoRankModel {

    private List<AnimeInfo> animeInfoList;
    private Integer totalCount;

    public AnimeInfoRankModel(List<AnimeInfo> animeInfoList, Integer totalCount) {
        this.animeInfoList = animeInfoList;
        this.totalCount = totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<AnimeInfo> getAnimeInfoList() {
        return animeInfoList;
    }

    public void setAnimeInfoList(List<AnimeInfo> animeInfoList) {
        this.animeInfoList = animeInfoList;
    }
}
