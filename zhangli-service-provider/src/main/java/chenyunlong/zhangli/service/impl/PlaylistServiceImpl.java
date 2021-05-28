package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import chenyunlong.zhangli.service.PlaylistService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final AnimePlaylistMapper animePlaylistMapper;

    public PlaylistServiceImpl(AnimePlaylistMapper animePlaylistMapper) {
        this.animePlaylistMapper = animePlaylistMapper;
    }

    @Override
    public List<PlayListDTO> listPlayListBy(Long animeId) {
        QueryWrapper<PlaylistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anime_id", animeId);
        List<PlaylistEntity> playlistEntities = animePlaylistMapper.selectList(queryWrapper);
        return convertToListVo(playlistEntities);
    }

    @Override
    public PlayListDTO getById(Long playlistId) {
        PlaylistEntity playlistEntity = animePlaylistMapper.selectById(playlistId);
        if (playlistEntity == null) {
            return null;
        }
        return new PlayListDTO().convertFrom(playlistEntity);
    }

    public List<PlayListDTO> convertToListVo(List<PlaylistEntity> episodeEntities) {
        return episodeEntities.stream().map(episode
                -> (PlayListDTO) new PlayListDTO().convertFrom(episode)).collect(Collectors.toList());
    }
}
