package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.dto.EpisodeDTO;
import chenyunlong.zhangli.model.params.AddEpisodeParam;

import java.util.List;

public interface AnimeEpisodeService {

    /**
     * 通过动漫ID查询动漫内部的所有播放信息
     *
     * @param animeId 动漫ID
     * @return 动漫的单集信息集合
     */
    List<EpisodeDTO> listEpisodeBy(Long animeId);

    /**
     * 视频信息
     *
     * @param episodeParam 添加视频信息
     * @return 动漫信息
     */
    AnimeEpisodeDTO add(AddEpisodeParam episodeParam);
}
