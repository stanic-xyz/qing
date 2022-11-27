/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.controller.content.model;

import cn.chenyunlong.qing.domain.anime.AnimeEpisodeEntity;
import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.PlaylistEntity;
import cn.chenyunlong.qing.domain.anime.model.dto.AnimeInfoMinimalDTO;
import cn.chenyunlong.qing.domain.anime.model.dto.AnimeInfoUpdateDTO;
import cn.chenyunlong.qing.domain.anime.model.vo.AnimeInfoPlayVo;
import cn.chenyunlong.qing.domain.anime.model.vo.AnimeInfoVo;
import cn.chenyunlong.qing.domain.anime.service.*;
import cn.chenyunlong.qing.infrastructure.model.Pagination;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeCommentDTO;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.infrastructure.model.dto.PlayListDTO;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.infrastructure.model.vo.OptionsModel;
import cn.chenyunlong.qing.infrastructure.model.vo.page.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AnimeInfoModel {
    private final AnimeInfoService animeInfoService;
    private final AnimeOptionsService optionService;
    private final AnimeOptionsService animeOptionsService;
    private final AnimeCommentService animeCommentService;
    private final AnimeEpisodeService episodeService;
    private final AnimeListService animeListService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * 首页数据展示结构
     */
    public IndexModel getIndex() throws InterruptedException {
        IndexModel indexModel = new IndexModel();
        //获取更新
        Future<List<AnimeInfoMinimalDTO>> recentUpdate = executorService.submit(() -> animeInfoService.getRecentUpdate(optionService.getRecentPageSize()));
        //获取推荐
        Future<List<AnimeInfoMinimalDTO>> listUpdate = executorService.submit(animeInfoService::getRecommendAnimeInfoList);
        //获取更新
        Future<List<AnimeInfoMinimalDTO>> listRecommend = executorService.submit(animeInfoService::getRecommendAnimeInfoList);
        //获取更新
        Future<List<AnimeInfoUpdateDTO>> update = executorService.submit(animeInfoService::getUpdateInfo);

        try {
            List<AnimeInfoMinimalDTO> dads = recentUpdate.get();
            Map<Integer, List<AnimeInfoMinimalDTO>> collect = dads.stream().collect(Collectors.groupingBy(animeInfoMinimalDTO -> animeInfoMinimalDTO.getPremiereDate().getDayOfWeek().getValue()));
            indexModel.setRecentList(dads);
            indexModel.setRecentMap(collect);
            indexModel.setUpdateInfoList(update.get());
            indexModel.setDalyUpdateList(listUpdate.get());
            indexModel.setRecommendList(listRecommend.get());
        } catch (InterruptedException exception) {
            log.warn("Interrupted", exception);
            throw exception;
        } catch (ExecutionException exception) {
            log.error("异步线程执行发生了错误", exception);
        }
        return indexModel;
    }

    /**
     * 获取详情界面
     *
     * @param animeId 动漫id
     * @return {@link DetailModel}
     */
    public DetailModel detail(Long animeId) {
        DetailModel detailModel = new DetailModel();
        AnimeInfo animeInfo = animeInfoService.getById(animeId);
        if (animeInfo != null) {
            AnimeInfoVo animeInfoVo = new AnimeInfoVo().convertFrom(animeInfo);
            List<PlaylistEntity> animePlayList = animeListService.getAnimePlayList(animeId);
            List<AnimeInfoMinimalDTO> recommendAnimeInfoList = animeInfoService.getRecommendAnimeInfoList();
            //获取前十条评论信息
            IPage<AnimeCommentDTO> commentsByAnimeId = animeCommentService.getCommentsByAnimeId(animeId, 1, 10);
            List<AnimeEpisodeEntity> episodeEntities = episodeService.getByAnimeId(animeId);

            List<PlayListDTO> playListList = animePlayList.stream().map(playlistEntity ->
            {
                PlayListDTO playListDTO = new PlayListDTO().convertFrom(playlistEntity);
                List<AnimeEpisodeDTO> animeEpisodeEntityStream = episodeEntities.stream()
                        .filter(animeEpisodeEntity -> Objects.equals(animeEpisodeEntity.getListId(), playlistEntity.getId()))
                        .map(animeEpisodeEntity -> (AnimeEpisodeDTO) new AnimeEpisodeDTO().convertFrom(animeEpisodeEntity))
                        .collect(Collectors.toList());
                playListDTO.setEpisodeList(animeEpisodeEntityStream);
                return playListDTO;
            }).collect(Collectors.toList());
            animeInfoVo.setPlayList(playListList);
            detailModel.setAnimeInfo(animeInfoVo);
            detailModel.setRelevant(recommendAnimeInfoList);
            detailModel.setRecommendation(recommendAnimeInfoList);
            detailModel.setComments(commentsByAnimeId);
        }
        return detailModel;
    }

    public PlayModel play(Long animeId, Long playId) {
        PlayModel playModel = new PlayModel();
        AnimeInfo animeInfo = animeInfoService.getById(animeId);

        if (animeInfo != null) {
            AnimeInfoVo animeInfoVo = new AnimeInfoVo().convertFrom(animeInfo);
            List<PlayListDTO> objectList = animeListService.getAnimePlayList(animeId).stream().map(playlistEntity -> (PlayListDTO) new PlayListDTO().convertFrom(playlistEntity)).collect(Collectors.toList());
            animeInfoVo.setPlayList(objectList);
            playModel.setAnimeInfo(new AnimeInfoPlayVo().convertFrom(animeInfo));
            playModel.setRelevant(animeInfoService.getRecommendAnimeInfoList());
            playModel.setRecommendation(animeInfoService.getRecommendAnimeInfoList());
            //获取前十条评论信息
            playModel.setComments(animeCommentService.getCommentsByAnimeId(animeId, 1, 10));
        }

        playModel.setRelevant(animeInfoService.getRecommendAnimeInfoList());
        playModel.setRecommendation(animeInfoService.getRecommendAnimeInfoList());
        //获取前十条评论信息
        playModel.setComments(animeCommentService.getCommentsByAnimeId(animeId, 1, 10));
        if (playId != null) {
            AnimeEpisodeEntity episodeEntity = episodeService.getById(playId);
            if (episodeEntity != null) {
                playModel.setEpisodeInfo(new AnimeEpisodeDTO().convertFrom(episodeEntity));
            }
        }


        return playModel;
    }

    /**
     * 最近更新界面
     *
     * @param page     分页参数
     * @param pageSize 分页大小
     * @return 更新界面的数据
     */
    public UpdateModel listUpdate(Integer page, Integer pageSize) {
        UpdateModel updateModel = new UpdateModel();
        IPage<AnimeInfoMinimalDTO> animeInfoPage = animeInfoService.getUpdateAnimeInfo(page, pageSize);
        updateModel.setAnimeList(animeInfoPage.getRecords());
        updateModel.setPagination(new Pagination(animeInfoPage));
        return updateModel;
    }

    public CatalogModel listCatalog(Page<AnimeInfo> objectPage, AnimeInfoQuery animeInfoQuery) {
        CatalogModel catalogModel = new CatalogModel();
        IPage<AnimeInfoVo> animeInfoPage = animeInfoService.listByPage(objectPage, animeInfoQuery);
        OptionsModel animeOptionsModel = animeOptionsService.getOptions();
        catalogModel.setQuery(animeInfoQuery);
        catalogModel.setYears(animeOptionsModel.getYears());
        catalogModel.setOptions(animeOptionsModel);
        catalogModel.setAnimeList(animeInfoPage.getRecords());
        catalogModel.setTotal(animeInfoPage.getTotal());
        catalogModel.setPagination(new Pagination(animeInfoPage));
        return catalogModel;
    }

    /**
     * 搜索模块
     *
     * @param objectPage     分页信息
     * @param animeInfoQuery 查询参数信息
     * @return 查询页配置信息
     */
    public SearchModel searchModel(Page<AnimeInfo> objectPage, AnimeInfoQuery animeInfoQuery) {
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
