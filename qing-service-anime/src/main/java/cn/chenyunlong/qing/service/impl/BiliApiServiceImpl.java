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

import cn.chenyunlong.qing.service.external.BiliService;
import cn.chenyunlong.qing.mapper.BiliAnimeMapper;
import cn.chenyunlong.qing.model.bilibili.BiliAnime;
import cn.chenyunlong.qing.model.entities.bilibili.BiliAnimeInfoEntity;
import cn.chenyunlong.qing.model.entities.bilibili.BilibiliAnimeScoreEntity;
import cn.chenyunlong.qing.service.BilibiliAnimeScoreService;
import cn.chenyunlong.qing.service.BilibiliAnimeService;
import cn.chenyunlong.qing.utils.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stan
 */
@Service
public class BiliApiServiceImpl implements BilibiliAnimeService {

    //调用B站的后台接口获取数据
    private final BiliService biliService;
    private final BiliAnimeMapper biliAnimeMapper;
    private final BilibiliAnimeScoreService animeScoreService;

    public BiliApiServiceImpl(BiliService biliService, BiliAnimeMapper biliAnimeMapper, BilibiliAnimeScoreService animeScoreService) {
        this.biliService = biliService;
        this.biliAnimeMapper = biliAnimeMapper;
        this.animeScoreService = animeScoreService;
    }

    @Override
    public void updateAnimeInfo() {
        List<BiliAnime> animeList = biliService.getBiliAnimeList();

        List<BiliAnimeInfoEntity> animeInfoEntities = animeList.stream()
                .map(animeInfo -> {
                    BiliAnimeInfoEntity animeInfoEntity = BeanUtils.transformFrom(animeInfo, BiliAnimeInfoEntity.class);
                    assert animeInfoEntity != null;
                    animeInfoEntity.setScore(StringUtils.hasText(animeInfo.getOrder()) ? Double.parseDouble(animeInfo.getOrder().replace("分", "")) : 0);
                    return animeInfoEntity;
                }).collect(Collectors.toList());

        QueryWrapper<BiliAnimeInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct (media_id) as media_id");
        List<BiliAnimeInfoEntity> entities = biliAnimeMapper.selectList(queryWrapper);
        animeInfoEntities.forEach(animeInfo -> {
            if (entities.stream().noneMatch(biliAnimeInfoEntity -> biliAnimeInfoEntity.getMediaId().equals(animeInfo.getMediaId()))) {
                //添加动漫信息
                biliAnimeMapper.insert(animeInfo);
            } else {
                //更新评分信息
                LambdaQueryWrapper<BiliAnimeInfoEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(BiliAnimeInfoEntity::getMediaId, animeInfo.getMediaId());
                lambdaQueryWrapper.last("limit 1");
                BiliAnimeInfoEntity animeInfoEntity = biliAnimeMapper.selectOne(lambdaQueryWrapper);
                if (animeInfoEntity != null) {
                    animeInfoEntity.setScore(animeInfo.getScore());
                    biliAnimeMapper.updateById(animeInfoEntity);
                }
            }
            //更新评分信息
            animeScoreService.updateAnimeScore(animeInfo.getMediaId(), animeInfo.getScore());
        });
    }

    @Override
    public List<BilibiliAnimeScoreEntity> getScoreInfoList(Long animeId, LocalDateTime startTime, LocalDateTime entTime) {
        BiliAnimeInfoEntity animeInfoEntity = biliAnimeMapper.selectById(animeId);
        if (animeInfoEntity == null) {
            return Collections.emptyList();
        }
        return animeScoreService.lambdaQuery()
                .eq(BilibiliAnimeScoreEntity::getAnimeId, animeInfoEntity.getMediaId())
                .ge(startTime != null, BilibiliAnimeScoreEntity::getRecordTime, startTime)
                .le(entTime != null, BilibiliAnimeScoreEntity::getRecordTime, entTime)
                .orderByAsc(BilibiliAnimeScoreEntity::getRecordTime)
                .list();
    }
}
