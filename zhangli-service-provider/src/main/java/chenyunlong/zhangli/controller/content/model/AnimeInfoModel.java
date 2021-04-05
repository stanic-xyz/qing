package chenyunlong.zhangli.controller.content.model;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.support.Pagination;
import chenyunlong.zhangli.model.vo.AnimeOptionsModel;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import chenyunlong.zhangli.model.vo.page.CatalogModel;
import chenyunlong.zhangli.model.vo.page.DetailModel;
import chenyunlong.zhangli.model.vo.page.IndexModel;
import chenyunlong.zhangli.model.vo.page.UpdateModel;
import chenyunlong.zhangli.service.AnimeInfoService;
import chenyunlong.zhangli.service.AnimeOptionsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimeInfoModel {
    private final AnimeInfoService animeInfoService;
    private final AnimeOptionsService optionService;
    private final AnimeOptionsService animeOptionsService;
    private final ZhangliProperties zhangliProperties;

    public AnimeInfoModel(AnimeInfoService animeInfoService, AnimeOptionsService optionService, AnimeOptionsService animeOptionsService, ZhangliProperties zhangliProperties) {
        this.animeInfoService = animeInfoService;
        this.optionService = optionService;
        this.animeOptionsService = animeOptionsService;
        this.zhangliProperties = zhangliProperties;
    }

    /**
     * 首页数据展示结构
     */
    public IndexModel listIndex() {
        int recentPageSize = optionService.getRecentPageSize();
        List<AnimeInfoMinimalDTO> recommendAnimeInfoList = animeInfoService.getRecommendAnimeInfoList();
        List<AnimeInfoMinimalDTO> recentAnimeInfoList = animeInfoService.getRecentUpdate(recentPageSize);
        IndexModel indexModel = new IndexModel();
        indexModel.setRecentList(recentAnimeInfoList);
        indexModel.setDalyUpdateList(recommendAnimeInfoList);
        indexModel.setRecommendList(recommendAnimeInfoList);
        return indexModel;
    }

    public DetailModel detail(Long animeId) {
        DetailModel detailModel = new DetailModel();

        AnimeInfoVo animeInfo = animeInfoService.convertToDetailVo(animeInfoService.getById(animeId));

        detailModel.setAnimeInfo(animeInfo);

        List<AnimeInfoMinimalDTO> relevantAnimeInfoList = animeInfoService.getRecommendAnimeInfoList();

        detailModel.setRelevant(relevantAnimeInfoList);

        List<AnimeInfoMinimalDTO> recommendAnimeInfoList = animeInfoService.getRecommendAnimeInfoList();

        detailModel.setRecommendation(recommendAnimeInfoList);
        return detailModel;
    }

    /**
     * 最近更新界面
     *
     * @param objectPage 分页参数
     * @return 更新界面的数据
     */
    public UpdateModel listUpdate(Page<AnimeInfo> objectPage) {

        UpdateModel updateModel = new UpdateModel();

        IPage<AnimeInfoVo> animeInfoPage = animeInfoService.getUpdateAnimeInfo(objectPage);

        updateModel.setAnimeList(animeInfoPage.getRecords());
        updateModel.setPagination(new Pagination(animeInfoPage));
        return updateModel;
    }

    public CatalogModel listCatalog(Page<AnimeInfo> objectPage, AnimeInfoQuery animeInfoQuery) {

        CatalogModel catalogModel = new CatalogModel();

        IPage<AnimeInfo> animeInfoIPage = animeInfoService.getRankPage(objectPage, animeInfoQuery);
        AnimeOptionsModel animeOptionsModel = animeOptionsService.getOptions();

        catalogModel.setQuery(animeInfoQuery);
        catalogModel.setYears(animeOptionsModel.getYears());
        catalogModel.setOptions(animeOptionsModel);
        catalogModel.setAnimeList(animeInfoIPage.getRecords());
        catalogModel.setTotal(animeInfoIPage.getTotal());
        catalogModel.setPagination(new Pagination(animeInfoIPage));
        return catalogModel;
    }
}
