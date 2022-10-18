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

package cn.chenyunlong.qing.service;


import cn.chenyunlong.qing.model.dto.anime.AnimeInfoMinimalDTO;
import cn.chenyunlong.qing.model.dto.anime.AnimeInfoRankDTO;
import cn.chenyunlong.qing.model.dto.anime.AnimeInfoUpdateDTO;
import cn.chenyunlong.qing.model.entities.AnimeComment;
import cn.chenyunlong.qing.model.entities.anime.AnimeInfo;
import cn.chenyunlong.qing.model.params.AnimeInfoQuery;
import cn.chenyunlong.qing.model.vo.anime.AnimeInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * @author Stan
 */

public interface AnimeInfoService extends IService<AnimeInfo> {

    /**
     * 获取排行榜信息
     *
     * @param pageInfo       分页信息
     * @param animeInfoQuery 查询条件
     * @return 动漫信息
     */
    IPage<AnimeInfoRankDTO> getRankPage(IPage<AnimeInfo> pageInfo, AnimeInfoQuery animeInfoQuery);

    /**
     * 添加动漫信息
     *
     * @param animeInfo 动漫信息
     * @return 创建的动漫详情
     */
    AnimeInfoVo create(AnimeInfo animeInfo);

    /**
     * 查询动画信息
     *
     * @param pageable 分页信息
     * @param query    查询参数（名称)
     * @return 满足条件的动画信息
     */
    IPage<AnimeInfoVo> listByPage(IPage<AnimeInfo> pageable, AnimeInfoQuery query);

    /**
     * 更新动漫信息
     *
     * @param animeInfo 动漫信息
     * @return 更新后的动漫详情信息
     */
    AnimeInfoVo updateBy(AnimeInfo animeInfo);

    /**
     * 删除动漫信息
     *
     * @param animeId 动漫ID
     */
    void deleteAnime(Long animeId);

    /**
     * 获取更新信息
     *
     * @param page     当前页
     * @param pageSize 分页大小
     * @return 动漫分页信息了
     */
    IPage<AnimeInfoMinimalDTO> getUpdateAnimeInfo(Integer page, Integer pageSize);

    /**
     * 获取推荐用户列表
     *
     * @return 推荐动漫列表
     */
    List<AnimeInfoMinimalDTO> getRecommendAnimeInfoList();


    /**
     * 获取最近更新的动漫列表
     *
     * @param recentPageSize 获取的动漫条数
     * @return 最近更新的动漫信息
     */
    List<AnimeInfoMinimalDTO> getRecentUpdate(int recentPageSize);

    /**
     * 下载图片
     *
     * @throws IOException 创建文件异常
     */
    void downloadImages() throws IOException;

    /**
     * 获取动漫的评论信息
     *
     * @param animeId   动漫ID
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return 平均信息列表
     */
    IPage<AnimeComment> getComment(Long animeId, Integer pageIndex, Integer pageSize);

    /**
     * 获取最近的动漫信息
     *
     * @return 动漫信息（最基础的信息）
     */
    List<AnimeInfoUpdateDTO> getUpdateInfo();

    /**
     * 添加评论信息
     *
     * @param cid     动漫ID
     * @param content 平均内容
     * @param user    用户名
     */
    void addComment(Long cid, String content, String user);

}
