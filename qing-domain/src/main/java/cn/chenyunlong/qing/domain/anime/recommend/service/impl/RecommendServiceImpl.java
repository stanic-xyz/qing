package cn.chenyunlong.qing.domain.anime.recommend.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import cn.chenyunlong.qing.domain.anime.recommend.dto.creator.RecommendCreator;
import cn.chenyunlong.qing.domain.anime.recommend.dto.query.RecommendQuery;
import cn.chenyunlong.qing.domain.anime.recommend.dto.updater.RecommendUpdater;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendDetailVO;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendVO;
import cn.chenyunlong.qing.domain.anime.recommend.mapper.RecommendMapper;
import cn.chenyunlong.qing.domain.anime.recommend.repository.RecommendRepository;
import cn.chenyunlong.qing.domain.anime.recommend.service.IRecommendService;
import cn.hutool.core.lang.Assert;
import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class RecommendServiceImpl implements IRecommendService {

    @Resource
    private final RecommendRepository recommendRepository;
    @Resource
    private final AnimeRepository animeRepository;

    @Override
    public Long createRecommend(RecommendCreator creator) {
        Optional<Recommend> recommend = EntityOperations.doCreate(recommendRepository)
                                            .create(() -> {
                                                Optional<Anime> optionalAnime = animeRepository.findById(creator.getAnimeId());
                                                if (optionalAnime.isEmpty()) {
                                                    throw new IllegalArgumentException("anime not found");
                                                }
                                                Anime anime = optionalAnime.get();
                                                Recommend oldRecommend = recommendRepository.findByAnimeId(anime.getId());
                                                Assert.isNull(oldRecommend, "该动漫信息已经在推荐列表中了");
                                                Recommend newRecommend = RecommendMapper.INSTANCE.dtoToEntity(creator);
                                                newRecommend.setAnimeId(anime.getId());
                                                return newRecommend;
                                            })
                                            .update(Recommend::init)
                                            .execute();
        return recommend.isPresent() ? recommend.get().getId() : 0;
    }

    @Override
    public void updateRecommend(RecommendUpdater updater) {
        EntityOperations.doUpdate(recommendRepository)
            .loadById(updater.getId())
            .update(updater::updateRecommend)
            .execute();
    }

    @Override
    public void validRecommend(Long id) {
        EntityOperations.doUpdate(recommendRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidRecommend(Long id) {
        EntityOperations.doUpdate(recommendRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public RecommendVO findById(Long id) {
        Optional<Recommend> recommend = recommendRepository.findById(id);

        return new RecommendVO(
            recommend.orElseThrow(() -> new BusinessException(CodeEnum.NotFindError)));
    }

    /**
     * findByPage
     */
    @Override
    public Page<RecommendDetailVO> findByPage(PageRequestWrapper<RecommendQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        Page<Recommend> recommendPage = recommendRepository.findAll(pageRequest);
        List<Recommend> content = recommendPage.getContent();
        Map<Long, Anime> animeMap = new HashMap<>();
        if (!content.isEmpty()) {
            List<Long> animeIds = content.stream().map(Recommend::getAnimeId).distinct().toList();
            List<Anime> animeList = animeRepository.findByIds(animeIds);
            animeMap.putAll(
                animeList.stream().collect(Collectors.toMap(Anime::getId, anime -> anime)));
        }
        return recommendPage.map(source -> {
            Anime orDefault = animeMap.getOrDefault(source.getAnimeId(), null);
            return new RecommendDetailVO(source, orDefault);
        });
    }


    /**
     * findByPage
     */
    @Override
    public List<RecommendDetailVO> listCommendAnime(LocalDate queryDate) {
        List<Recommend> recommendList = recommendRepository.queryRecommendByDateEquals(queryDate);
        Map<Long, Anime> animeMap = new HashMap<>();
        if (!recommendList.isEmpty()) {
            List<Long> animeIds = recommendList.stream().map(Recommend::getAnimeId).distinct().toList();
            List<Anime> animeList = animeRepository.findByIds(animeIds);
            animeMap.putAll(
                animeList.stream().collect(Collectors.toMap(Anime::getId, anime -> anime)));
        }
        return recommendList.stream().map(source -> {
            Anime anime = animeMap.getOrDefault(source.getAnimeId(), null);
            return new RecommendDetailVO(source, anime);
        }).toList();
    }


}
