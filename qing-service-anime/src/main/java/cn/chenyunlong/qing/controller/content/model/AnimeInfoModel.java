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

import cn.chenyunlong.qing.domain.anime.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeInfoMapper;
import cn.chenyunlong.qing.domain.anime.anime.model.dto.AnimeInfoMinimalDTO;
import cn.chenyunlong.qing.domain.anime.anime.model.dto.AnimeInfoUpdateDTO;
import cn.chenyunlong.qing.domain.anime.anime.model.vo.AnimeInfoPlayVo;
import cn.chenyunlong.qing.domain.anime.anime.model.vo.AnimeInfoVo;
import cn.chenyunlong.qing.domain.anime.anime.response.AnimeInfoResponse;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeInfoService;
import cn.chenyunlong.qing.domain.anime.anime.vo.AnimeInfoVO;
import cn.chenyunlong.qing.domain.anime.comment.service.ICommentService;
import cn.chenyunlong.qing.domain.anime.comment.vo.CommentVO;
import cn.chenyunlong.qing.domain.anime.domainservice.AnimeOptionsService;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.service.IEpisodeService;
import cn.chenyunlong.qing.domain.anime.playlist.Playlist;
import cn.chenyunlong.qing.infrastructure.model.Pagination;
import cn.chenyunlong.qing.infrastructure.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.infrastructure.model.dto.PlayListDTO;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.infrastructure.model.vo.OptionsModel;
import cn.chenyunlong.qing.infrastructure.model.vo.page.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
    private final IAnimeInfoService animeInfoService;
    private final AnimeOptionsService optionService;
    private final AnimeOptionsService animeOptionsService;
    private final ICommentService commentService;
    private final IEpisodeService episodeService;
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
        AnimeInfoVO animeInfo = animeInfoService.findById(animeId);
        if (animeInfo != null) {
            AnimeInfoResponse infoResponse = AnimeInfoMapper.INSTANCE.vo2CustomResponse(animeInfo);
            List<Playlist> animePlayList = Collections.emptyList();
            List<AnimeInfoMinimalDTO> recommendAnimeInfoList = animeInfoService.getRecommendAnimeInfoList();
            //获取前十条评论信息
            Page<CommentVO> byPage = commentService.findByPage(null);

            List<Episode> episodeEntities = Collections.emptyList();

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
            AnimeInfoVo animeInfoVo = new AnimeInfoVo();
            animeInfoVo.setPlayList(playListList);
            detailModel.setAnimeInfo(animeInfoVo);
            detailModel.setRelevant(recommendAnimeInfoList);
            detailModel.setRecommendation(recommendAnimeInfoList);
            detailModel.setComments(Collections.emptyList());
        }
        return detailModel;
    }

    public PlayModel play(Long animeId, Long listId, Long playId) {
        PlayModel playModel = new PlayModel();
        AnimeInfo animeInfo = animeInfoService.getById(animeId);

        if (animeInfo != null) {
            AnimeInfoVo animeInfoVo = new AnimeInfoVo().convertFrom(animeInfo);

            // TODO 暂时空置
            List<PlayListDTO> objectList = Collections.emptyList();
            animeInfoVo.setPlayList(objectList);
            playModel.setAnimeInfo(new AnimeInfoPlayVo().convertFrom(animeInfo));
            playModel.setRelevant(animeInfoService.getRecommendAnimeInfoList());
            playModel.setRecommendation(animeInfoService.getRecommendAnimeInfoList());
            //获取前十条评论信息
            playModel.setComments(Collections.emptyList());
        }

        playModel.setRelevant(animeInfoService.getRecommendAnimeInfoList());
        playModel.setRecommendation(animeInfoService.getRecommendAnimeInfoList());
        //获取前十条评论信息
        playModel.setComments(Collections.emptyList());
        if (playId != null) {
            Episode episodeEntity = null;
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
        Page<AnimeInfoMinimalDTO> animeInfoPage = new PageImpl<>(Collections.emptyList());
        updateModel.setAnimeList(animeInfoPage.getContent());
        updateModel.setPagination(new Pagination());
        return updateModel;
    }

    public CatalogModel listCatalog() {
        CatalogModel catalogModel = new CatalogModel();
        Page<AnimeInfoVo> animeInfoPage = new PageImpl<>(Collections.emptyList());
        OptionsModel animeOptionsModel = animeOptionsService.getOptions();
        catalogModel.setQuery(null);
        catalogModel.setYears(animeOptionsModel.getYears());
        catalogModel.setOptions(animeOptionsModel);
        catalogModel.setAnimeList(Collections.emptyList());
        catalogModel.setTotal(0L);
        catalogModel.setPagination(new Pagination());
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

        Page<AnimeInfo> animeInfoPage = new PageImpl<>(Collections.emptyList());
        OptionsModel animeOptionsModel = animeOptionsService.getOptions();
        searchModel.setQuery(animeInfoQuery);
        searchModel.setOptions(animeOptionsModel);
        searchModel.setAnimeInfos(Collections.emptyList());
        searchModel.setTotalCount(0L);
        searchModel.setCurrentIndex(0L);
        searchModel.setPagination(new Pagination());
        searchModel.setTotalPage(2L);
        return searchModel;
    }
}
