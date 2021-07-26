package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.params.AddEpisodeParam;

import java.util.List;

public interface AnimeEpisodeService {

    /**
     * 通过动漫ID查询动漫内部的所有播放信息
     *
     * @param animeId 动漫ID
     * @return 动漫的单集信息集合
     */
    List<AnimeEpisodeDTO> listEpisodeByAnimeId(Long animeId);

    /**
     * 视频信息
     *
     * @param episodeParam 添加视频信息
     * @return 动漫信息
     */
    AnimeEpisodeDTO add(AddEpisodeParam episodeParam);

    /**
     * 获取视频播放详情
     *
     * @param playId 播放视频ID
     * @return 视频播放详情
     */
    AnimeEpisodeDTO getById(Long playId);
}
