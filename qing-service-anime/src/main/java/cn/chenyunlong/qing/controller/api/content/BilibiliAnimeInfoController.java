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

import cn.chenyunlong.qing.model.entities.bilibili.BilibiliAnimeScoreEntity;
import cn.chenyunlong.qing.service.BilibiliAnimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/anime/bilibili")
public class BilibiliAnimeInfoController {

    private final BilibiliAnimeService animeInfoService;

    public BilibiliAnimeInfoController(BilibiliAnimeService animeInfoService) {
        this.animeInfoService = animeInfoService;
    }

    @GetMapping("scores")
    public List<BilibiliAnimeScoreEntity> getScoreList(Long animeId, LocalDateTime startTime, LocalDateTime endTime) {
        return animeInfoService.getScoreInfoList(animeId, startTime, endTime);
    }

    @GetMapping("update")
    public void updateAnimeInfo() {
        animeInfoService.updateAnimeInfo();
    }
}
