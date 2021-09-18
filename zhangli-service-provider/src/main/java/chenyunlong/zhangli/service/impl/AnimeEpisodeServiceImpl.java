package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.AnimeEpisodeMapper;
import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.model.params.AddEpisodeParam;
import chenyunlong.zhangli.service.AnimeEpisodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeEpisodeServiceImpl extends ServiceImpl<AnimeEpisodeMapper, AnimeEpisodeEntity> implements AnimeEpisodeService {

    private final AnimeEpisodeMapper episodeMapper;

    public AnimeEpisodeServiceImpl(AnimeEpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    @Override
    public List<AnimeEpisodeDTO> listEpisodeByAnimeId(Long animeId) {
        QueryWrapper<AnimeEpisodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anime_id", animeId);
        return episodeMapper.listEpisodeByAnimeId(queryWrapper);
    }

    @Override
    public AnimeEpisodeDTO add(AddEpisodeParam episodeParam) {
        AnimeEpisodeEntity episodeEntity = episodeParam.convertTo();
        //设置状态，刚上传
        episodeEntity.setStatus(0);
        episodeMapper.insert(episodeEntity);
        return new AnimeEpisodeDTO().convertFrom(episodeEntity);
    }

    @Override
    public List<AnimeEpisodeEntity> getByAnimeId(Long animeId) {
        return lambdaQuery()
                .eq(AnimeEpisodeEntity::getAnimeId, animeId)
                .list();

    }

}
