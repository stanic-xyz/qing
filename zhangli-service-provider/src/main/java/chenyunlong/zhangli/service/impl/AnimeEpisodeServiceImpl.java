package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.exception.ServiceException;
import chenyunlong.zhangli.mapper.AnimeEpisodeMapper;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.dto.EpisodeDTO;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AddEpisodeParam;
import chenyunlong.zhangli.service.AnimeEpisodeService;
import chenyunlong.zhangli.service.PlaylistService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimeEpisodeServiceImpl implements AnimeEpisodeService {

    private final AnimeEpisodeMapper episodeMapper;
    private final AnimeInfoMapper animeInfoMapper;
    private final PlaylistService playlistService;

    public AnimeEpisodeServiceImpl(AnimeEpisodeMapper episodeMapper,
                                   AnimeInfoMapper animeInfoMapper,
                                   PlaylistService playlistService) {
        this.episodeMapper = episodeMapper;
        this.animeInfoMapper = animeInfoMapper;
        this.playlistService = playlistService;
    }

    @Override
    public List<EpisodeDTO> listEpisodeBy(Long animeId) {
        QueryWrapper<AnimeEpisodeEntity> queryWrapper = new QueryWrapper<>();

        List<AnimeEpisodeEntity> episodeEntities = episodeMapper.selectList(queryWrapper);

        return convertToListVo(episodeEntities);
    }

    @Override
    public AnimeEpisodeDTO add(AddEpisodeParam episodeParam) {
        AnimeEpisodeEntity episodeEntity = episodeParam.convertTo();

        AnimeInfo animeInfo = animeInfoMapper.selectById(episodeEntity.getAnimeId());
        if (animeInfo == null) {
            throw new ServiceException("动漫信息不存在");
        }
        //设置状态，刚上传
        episodeEntity.setStatus(0);
        episodeMapper.insert(episodeEntity);
        return new AnimeEpisodeDTO().convertFrom(episodeEntity);
    }

    public List<EpisodeDTO> convertToListVo(List<AnimeEpisodeEntity> episodeEntities) {
        return episodeEntities.stream().map(episode
                -> (EpisodeDTO) new EpisodeDTO().convertFrom(episode)).collect(Collectors.toList());
    }
}
