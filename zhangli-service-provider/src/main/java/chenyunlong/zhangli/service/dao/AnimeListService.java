package chenyunlong.zhangli.service.dao;

import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeListService extends IService<PlaylistEntity> {

    /**
     * 根据动漫ID获取播放列表
     *
     * @param animeId 动漫ID
     * @return 播放列表
     */
    List<PlaylistEntity> getAnimePlayList(Long animeId);
}
