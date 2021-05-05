package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.entities.anime.AnimePlaylistEntity;
import chenyunlong.zhangli.service.PlaylistService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final AnimePlaylistMapper animePlaylistMapper;

    public PlaylistServiceImpl(AnimePlaylistMapper animePlaylistMapper) {
        this.animePlaylistMapper = animePlaylistMapper;
    }

    @Override
    public List<PlayListDTO> listPlayListBy(Long animeId) {
        QueryWrapper<AnimePlaylistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anime_id", animeId);
        List<AnimePlaylistEntity> playlistEntities = animePlaylistMapper.selectList(queryWrapper);
        return convertToListVo(playlistEntities);
    }

    public List<PlayListDTO> convertToListVo(List<AnimePlaylistEntity> episodeEntities) {
        return episodeEntities.stream().map(episode
                -> (PlayListDTO) new PlayListDTO().convertFrom(episode)).collect(Collectors.toList());
    }
}