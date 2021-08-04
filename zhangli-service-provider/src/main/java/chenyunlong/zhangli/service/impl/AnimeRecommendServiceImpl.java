package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.model.entities.AnimeRecommendEntity;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.mapper.AnimeRecommendMapper;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.params.AnimeRecommendParam;
import chenyunlong.zhangli.service.AnimeRecommendService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import chenyunlong.zhangli.core.exception.ServiceException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeRecommendServiceImpl extends ServiceImpl<AnimeRecommendMapper, AnimeRecommendEntity> implements AnimeRecommendService {

    private final AnimeRecommendMapper animeRecommendMapper;

    private final AnimeInfoMapper animeInfoMapper;

    public AnimeRecommendServiceImpl(AnimeRecommendMapper animeRecommendMapper, AnimeInfoMapper animeInfoMapper) {
        this.animeRecommendMapper = animeRecommendMapper;
        this.animeInfoMapper = animeInfoMapper;
    }

    @Override
    public List<AnimeRecommendEntity> getRecommendAnimeInfoList(Pageable pageable, AnimeInfoQuery animeInfoQuery) {
//        return animeInfoMapper.listAnime(pageable, animeInfoQuery);
        return null;
    }

    @Override
    public AnimeRecommendEntity getById(Long recommendId) {
        return null;
    }

    @Override
    public AnimeRecommendEntity addRecommend(AnimeRecommendEntity recommendEntity) {

        AnimeInfo animeInfo = animeInfoMapper.selectById(recommendEntity.getAid());
        if (animeInfo == null) {
            throw new ServiceException("动漫信息不存在");
        }
        //已经添加到推荐列表了
        Integer count = lambdaQuery().eq(AnimeRecommendEntity::getAid, recommendEntity.getAid()).count();
        if (count != 0) {
            throw new ServiceException("已经添加到推荐列表");
        }
        recommendEntity.preCheck();
        boolean save = save(recommendEntity);
        if (save) {
            return recommendEntity;
        } else {
            throw new ServiceException("保存失败");
        }
    }

    @Override
    public IPage<AnimeRecommendEntity> pageBy(AnimeRecommendParam typeParam, Pageable pageable) {
        return null;
    }

    @Override
    public AnimeRecommendEntity update(AnimeRecommendEntity animeRecommendEntity) {
        return null;
    }
}
