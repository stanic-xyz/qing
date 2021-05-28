package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.dto.PlayListDTO;

import java.util.List;

/**
 * 播放合集服务接口
 */
public interface PlaylistService {
    /**
     * 根据动漫信息获取动漫的播放列表
     *
     * @param animeId 动漫ID
     * @return 播放列表
     */
    List<PlayListDTO> listPlayListBy(Long animeId);

    /**
     * 获取播放列表
     *
     * @param playlistId 播放列表ID
     * @return 播放列表信息
     */
    PlayListDTO getById(Long playlistId);
}
