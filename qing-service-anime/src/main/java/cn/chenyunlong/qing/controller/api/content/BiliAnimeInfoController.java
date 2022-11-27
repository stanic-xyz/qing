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

package cn.chenyunlong.qing.controller.api.content;

import cn.chenyunlong.qing.domain.third.bilibili.BilibiliAnimeScoreEntity;
import cn.chenyunlong.qing.domain.third.bilibili.service.BilibiliAnimeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 外部接口-哔哩哔哩动漫接口
 * 用于定时获取哔哩哔哩动画的番剧评分信息
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "哔哩哔哩动画")
@RestController
@RequestMapping("api/anime/bilibili")
@RequiredArgsConstructor
public class BiliAnimeInfoController {

    private final BilibiliAnimeService animeInfoService;

    /**
     * 得到评分列表
     *
     * @param animeId   动漫id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return {@link List}<{@link BilibiliAnimeScoreEntity}>
     */
    @GetMapping("scores")
    public List<BilibiliAnimeScoreEntity> getScoreList(Long animeId, LocalDateTime startTime, LocalDateTime endTime) {
        return animeInfoService.getScoreInfoList(animeId, startTime, endTime);
    }

    /**
     * 动漫更新信息
     */
    @GetMapping("update")
    public void updateAnimeInfo() {
        animeInfoService.updateAnimeInfo();
    }
}
