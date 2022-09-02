package cn.chenyunlong.zhangli.model.agefans;

import lombok.Data;

import java.util.List;

@Data
public class AgePlayList {
    private Integer index;
    List<AgeAnimeEpisode> episodeList;
}
