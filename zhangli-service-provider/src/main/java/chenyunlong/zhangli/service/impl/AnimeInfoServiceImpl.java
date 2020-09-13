package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.model.response.anime.AnimeInfoRankModel;
import chenyunlong.zhangli.service.AnimeInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeInfoServiceImpl implements AnimeInfoService {
    private final AnimeInfoMapper animeInfoMapper;

    public AnimeInfoServiceImpl(AnimeInfoMapper animeInfoMapper) {
        this.animeInfoMapper = animeInfoMapper;
    }

    @Override
    public AnimeInfoRankModel getRankPage(Pageable pageable) {
        List<AnimeInfo> animeInfoList = animeInfoMapper.listAnimes(pageable);
        Integer count = animeInfoMapper.count();
        return new AnimeInfoRankModel(animeInfoList, count);
    }

    @Override
    public void add(List<AnimeInfo> animeInfos) {

        animeInfos.forEach(animeInfo -> {
            animeInfoMapper.insertPatch(animeInfos);
        });

        animeInfoMapper.insertPatch(animeInfos);
    }

    @Override
    public AnimeInfo getMovieDetail(String movieId) {
        AnimeInfo animeInfo = animeInfoMapper.selectAnimationDetail(movieId);
        return animeInfo;
    }

}
