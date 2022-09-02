package cn.chenyunlong.zhangli.service;

import cn.chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import cn.chenyunlong.zhangli.model.params.AddEpisodeParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeEpisodeService extends IService<AnimeEpisodeEntity> {

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
     * 根据动漫ID获取播放视频列表
     *
     * @param animeId 动漫ID
     * @return 播放列表
     */
    List<AnimeEpisodeEntity> getByAnimeId(Long animeId);
}
