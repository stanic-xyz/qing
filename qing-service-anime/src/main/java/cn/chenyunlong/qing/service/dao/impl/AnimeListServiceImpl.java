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

package cn.chenyunlong.qing.service.dao.impl;

import cn.chenyunlong.qing.domain.anime.PlaylistEntity;
import cn.chenyunlong.qing.mapper.AnimePlaylistMapper;
import cn.chenyunlong.qing.service.dao.AnimeListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class AnimeListServiceImpl extends ServiceImpl<AnimePlaylistMapper, PlaylistEntity> implements AnimeListService {
    @Override
    public List<PlaylistEntity> getAnimePlayList(Long animeId) {
        return lambdaQuery().eq(PlaylistEntity::getAnimeId, animeId).list();
    }
}
