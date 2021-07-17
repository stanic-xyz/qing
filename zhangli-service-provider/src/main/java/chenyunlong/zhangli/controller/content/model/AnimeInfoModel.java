package chenyunlong.zhangli.controller.content.model;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.core.support.Pagination;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.vo.OptionsModel;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.model.vo.page.*;
import chenyunlong.zhangli.service.AnimeCommentService;
import chenyunlong.zhangli.service.AnimeEpisodeService;
import chenyunlong.zhangli.service.AnimeInfoService;
import chenyunlong.zhangli.service.AnimeOptionsService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Stan
 */
@Slf4j
@Component
public class AnimeInfoModel {
    private final AnimeInfoService animeInfoService;
    private final AnimeOptionsService optionService;
    private final AnimeOptionsService animeOptionsService;
    private final ZhangliProperties zhangliProperties;
    private final AnimeCommentService animeCommentService;
    private final AnimeEpisodeService episodeService;

    public AnimeInfoModel(AnimeInfoService animeInfoService, AnimeOptionsService optionService, AnimeOptionsService animeOptionsService, ZhangliProperties zhangliProperties, AnimeCommentService animeCommentService, AnimeEpisodeService episodeService) {
        this.animeInfoService = animeInfoService;
        this.optionService = optionService;
        this.animeOptionsService = animeOptionsService;
        this.zhangliProperties = zhangliProperties;
        this.animeCommentService = animeCommentService;
        this.episodeService = episodeService;
    }

    /**
     * 首页数据展示结构
     */
    public IndexModel listIndex() throws JsonProcessingException {
        IndexModel indexModel = new IndexModel();
        indexModel.setRecentList(animeInfoService.getRecentUpdate(optionService.getRecentPageSize()));
        indexModel.setDalyUpdateList(animeInfoService.getRecommendAnimeInfoList());
        indexModel.setRecommendList(animeInfoService.getRecommendAnimeInfoList());
        indexModel.setUpdateInfoList(animeInfoService.getUpdateInfo());
        return indexModel;
    }

    public DetailModel detail(Long animeId) {
        DetailModel detailModel = new DetailModel();
        detailModel.setAnimeInfo(animeInfoService.convertToDetailVo(animeInfoService.getById(animeId)));
        detailModel.setRelevant(animeInfoService.getRecommendAnimeInfoList());
        detailModel.setRecommendation(animeInfoService.getRecommendAnimeInfoList());
        //获取前十条评论信息
        detailModel.setComments(animeCommentService.getCommentsByAnimeId(animeId, 1, 10));
        return detailModel;
    }

    public PlayModel play(Long animeId, Long playId) {
        PlayModel playModel = new PlayModel();
        playModel.setAnimeInfo(animeInfoService.convertToPlayVo(animeInfoService.getById(animeId)));
        playModel.setRelevant(animeInfoService.getRecommendAnimeInfoList());
        playModel.setRecommendation(animeInfoService.getRecommendAnimeInfoList());
        //获取前十条评论信息
        playModel.setComments(animeCommentService.getCommentsByAnimeId(animeId, 1, 10));
        if (playId != null) {
            playModel.setEpisodeInfo(episodeService.getById(playId));
        }
        return playModel;
    }

    /**
     * 最近更新界面
     *
     * @param objectPage 分页参数
     * @return 更新界面的数据
     */
    public UpdateModel listUpdate(Page<AnimeInfo> objectPage) {

        if (log.isDebugEnabled()) {
            log.debug("测试身份验证地址：" + zhangliProperties.getAuthenticationPrefix());
        }
        UpdateModel updateModel = new UpdateModel();

        IPage<AnimeInfoVo> animeInfoPage = animeInfoService.getUpdateAnimeInfo(objectPage);

        updateModel.setAnimeList(animeInfoPage.getRecords());
        updateModel.setPagination(new Pagination(animeInfoPage));
        return updateModel;
    }

    public CatalogModel listCatalog(Page<AnimeInfo> objectPage, AnimeInfoQuery animeInfoQuery) {

        CatalogModel catalogModel = new CatalogModel();
        IPage<AnimeInfo> animeInfoPage = animeInfoService.getRankPage(objectPage, animeInfoQuery);
        OptionsModel animeOptionsModel = animeOptionsService.getOptions();

        catalogModel.setQuery(animeInfoQuery);
        catalogModel.setYears(animeOptionsModel.getYears());
        catalogModel.setOptions(animeOptionsModel);
        catalogModel.setAnimeList(animeInfoPage.getRecords());
        catalogModel.setTotal(animeInfoPage.getTotal());
        catalogModel.setPagination(new Pagination(animeInfoPage));
        return catalogModel;
    }

    public SearchModel searchModel(Page<AnimeInfo> objectPage, AnimeInfoQuery animeInfoQuery) {


//        long totalCount = animeInfoService.getTotalCount(query);
//        int totalPage = (int) Math.ceil(((double) totalCount) / ((double) pageSize));
//        animeInfoService.listByPage(new Page<>(page, pageSize), searchParam);

        SearchModel searchModel = new SearchModel();
        LambdaQueryWrapper<AnimeInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(AnimeInfo::getName, animeInfoQuery.getKeyword());

        IPage<AnimeInfo> animeInfoPage = animeInfoService.page(objectPage, queryWrapper);
        OptionsModel animeOptionsModel = animeOptionsService.getOptions();
        searchModel.setQuery(animeInfoQuery);
        searchModel.setOptions(animeOptionsModel);
        searchModel.setAnimeInfos(animeInfoPage.getRecords());
        searchModel.setTotalCount(animeInfoPage.getTotal());
        searchModel.setCurrentIndex(animeInfoPage.getCurrent());
        searchModel.setPagination(new Pagination(animeInfoPage));
        searchModel.setTotalPage(animeInfoPage.getPages());
        return searchModel;
    }
}
