package cn.chenyunlong.qing.domain.anime.recommend.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.recommend.dto.creator.RecommendCreator;
import cn.chenyunlong.qing.domain.anime.recommend.dto.query.RecommendQuery;
import cn.chenyunlong.qing.domain.anime.recommend.dto.updater.RecommendUpdater;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendDetailVO;
import cn.chenyunlong.qing.domain.anime.recommend.dto.vo.RecommendVO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IRecommendService {

    /**
     * create
     */
    Long createRecommend(RecommendCreator creator);

    /**
     * update
     */
    void updateRecommend(RecommendUpdater updater);

    void validRecommend(Long id);

    void invalidRecommend(Long id);

    /**
     * findById
     */
    RecommendVO findById(Long id);

    /**
     * findByPage
     */
    Page<RecommendDetailVO> findByPage(PageRequestWrapper<RecommendQuery> query);

    /**
     * 查询当天的推荐列表
     */
    List<RecommendDetailVO> listCommendAnime(LocalDate queryDate);
}
