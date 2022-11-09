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

package cn.chenyunlong.qing.service.impl;

import cn.chenyunlong.qing.domain.anime.AnimeEpisodeEntity;
import cn.chenyunlong.qing.mapper.AnimeEpisodeMapper;
import cn.chenyunlong.qing.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.model.params.AddEpisodeParam;
import cn.chenyunlong.qing.service.AnimeEpisodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeEpisodeServiceImpl extends ServiceImpl<AnimeEpisodeMapper, AnimeEpisodeEntity> implements AnimeEpisodeService {

    private final AnimeEpisodeMapper episodeMapper;

    public AnimeEpisodeServiceImpl(AnimeEpisodeMapper episodeMapper) {
        this.episodeMapper = episodeMapper;
    }

    @Override
    public List<AnimeEpisodeDTO> listEpisodeByAnimeId(Long animeId) {
        QueryWrapper<AnimeEpisodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("anime_id", animeId);
        return episodeMapper.listEpisodeByAnimeId(queryWrapper);
    }

    @Override
    public AnimeEpisodeDTO add(AddEpisodeParam episodeParam) {
        AnimeEpisodeEntity episodeEntity = episodeParam.convertTo();
        //设置状态，刚上传
        episodeEntity.setStatus(0);
        episodeMapper.insert(episodeEntity);
        return new AnimeEpisodeDTO().convertFrom(episodeEntity);
    }

    @Override
    public List<AnimeEpisodeEntity> getByAnimeId(Long animeId) {
        return lambdaQuery()
                .eq(AnimeEpisodeEntity::getAnimeId, animeId)
                .list();

    }

}
