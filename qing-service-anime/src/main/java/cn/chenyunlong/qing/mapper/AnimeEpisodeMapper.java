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

package cn.chenyunlong.qing.mapper;

import cn.chenyunlong.qing.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.model.entities.anime.AnimeEpisodeEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 */
@Mapper
@Component
public interface AnimeEpisodeMapper extends BaseMapper<AnimeEpisodeEntity> {
    /**
     * 获取某番剧的播放剧集
     *
     * @param animeId 番剧ID
     * @return 播放剧集信息
     */
    @Select("select * from anime_episode where anime_id=#{animeId}")
    List<AnimeEpisodeEntity> selectByAnimeId(@Param("animeId") Long animeId);

    List<AnimeEpisodeDTO> listEpisodeByAnimeId(@Param(Constants.WRAPPER) QueryWrapper<AnimeEpisodeEntity> queryWrapper);
}
