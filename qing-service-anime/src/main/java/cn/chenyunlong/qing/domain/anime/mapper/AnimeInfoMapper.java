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

package cn.chenyunlong.qing.domain.anime.mapper;

import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.infrastructure.model.params.AnimeInfoQuery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Stan
 * @date 2020/11/01
 */
@Mapper
@Component
public interface AnimeInfoMapper extends BaseMapper<AnimeInfo> {

    /**
     * 分页获取动画信息
     *
     * @param pageable       分页信息
     * @param animeInfoQuery 查询条件
     * @return 所有动画的分页信息
     */
    List<AnimeInfo> listAnime(@Param("page") Pageable pageable, @Param("query") AnimeInfoQuery animeInfoQuery);

    /**
     * 计算满足条件的记录个数
     *
     * @return 总数
     */
    Long count(@Param("query") AnimeInfoQuery animeInfoQuery);

    /**
     * 获取动漫详情
     *
     * @param movieId 动漫ID
     * @return 具体的动画信息
     */
    AnimeInfo selectAnimationDetail(Long movieId);

    /**
     * 根据动画名称查询动画信息
     *
     * @param animeInfoQuery 动画名称
     * @param offset         偏差值
     * @param pageSize       分页大小
     * @return 返回
     */
    List<AnimeInfo> selectAnimationW(@Param("vo") AnimeInfoQuery animeInfoQuery, Long offset, Integer pageSize);

    /**
     * 根据名称查询动漫信息
     *
     * @param name 动漫名称
     * @return 满足条件的动漫信息
     */
    long countByNameLike(String name);

    /**
     * 更新动漫信息
     *
     * @param animeInfo 动漫信息
     */
    void update(AnimeInfo animeInfo);

    /**
     * 获取连载中的动漫总数
     *
     * @return 连载中的动漫总数
     */
    long getUpdateAnimeCount();

    /**
     * @param pageRequest 分页对象
     * @return 连载中的动漫信息
     */
    List<AnimeInfo> selectAnimeByUpdateTime(@Param("pageRequest") Pageable pageRequest);

    /**
     * 获取最早的一部动漫年份（不包括年份不确定的动漫）
     *
     * @return 年份最早的番
     */
    LocalDate selectEarliestyear();

    /**
     * 获取推荐的动漫列表
     *
     * @return 推荐的动漫列表
     */
    List<AnimeInfo> listRecommendAnimeInfo();
}
