package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimeEpisodeMapper;
import chenyunlong.zhangli.model.dto.EpisodeDTO;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.service.AnimeEpisodeService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnimeEpisodeServiceImpl implements AnimeEpisodeService {

    private final AnimeEpisodeMapper episodeMapper;

    public AnimeEpisodeServiceImpl(AnimeEpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    @Override
    public List<EpisodeDTO> listEpisodeBy(Long animeId) {
        QueryWrapper<AnimeEpisodeEntity> queryWrapper = new QueryWrapper<>();

        List<AnimeEpisodeEntity> episodeEntities = episodeMapper.selectList(queryWrapper);

        return convertToListVo(episodeEntities);
    }

    public List<EpisodeDTO> convertToListVo(List<AnimeEpisodeEntity> episodeEntities) {
        return episodeEntities.stream().map(episode
                -> (EpisodeDTO) new EpisodeDTO().convertFrom(episode)).collect(Collectors.toList());
    }
}
