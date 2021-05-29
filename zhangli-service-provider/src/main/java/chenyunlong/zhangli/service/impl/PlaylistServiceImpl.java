package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import chenyunlong.zhangli.service.AnimeEpisodeService;
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
    private final AnimeEpisodeService episodeService;

    public PlaylistServiceImpl(AnimePlaylistMapper animePlaylistMapper,
                               AnimeEpisodeService episodeService) {
        this.animePlaylistMapper = animePlaylistMapper;
        this.episodeService = episodeService;
    }

    @Override
    public List<PlayListDTO> listPlayListBy(Long animeId) {
        QueryWrapper<PlaylistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anime_id", animeId);
        List<PlaylistEntity> playlistEntities = animePlaylistMapper.selectList(queryWrapper);
        QueryWrapper<AnimeEpisodeEntity> episodeQueryWrapper = new QueryWrapper<>();
        episodeQueryWrapper.eq("anime_id", animeId);
        List<AnimeEpisodeDTO> episodeDTOList = episodeService.listEpisodeByAnimeId(animeId);

        return convertToListVo(playlistEntities, episodeDTOList);
    }

    @Override
    public PlayListDTO getById(Long playlistId) {
        PlaylistEntity playlistEntity = animePlaylistMapper.selectById(playlistId);
        if (playlistEntity == null) {
            return null;
        }
        return new PlayListDTO().convertFrom(playlistEntity);
    }

    public List<PlayListDTO> convertToListVo(List<PlaylistEntity> playlistEntities, List<AnimeEpisodeDTO> episodeEntities) {
        //转换查询到的列表信息
        return playlistEntities.stream().map(episode ->
        {
            //查询列表中的视频信息
            PlayListDTO playListDTO = new PlayListDTO().convertFrom(episode);
            playListDTO.setEpisodeList(episodeEntities.stream().filter(animeEpisodeDTO -> animeEpisodeDTO.getAnimeId().equals(episode.getAnimeId())).collect(Collectors.toList()));
            return playListDTO;
        }).collect(Collectors.toList());
    }


}
