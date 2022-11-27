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

package cn.chenyunlong.qing.domain.third.bilibili.service;

import cn.chenyunlong.qing.domain.third.bilibili.model.AnimeData;
import cn.chenyunlong.qing.domain.third.bilibili.model.BgngumeResponse;
import cn.chenyunlong.qing.domain.third.bilibili.model.BiliAnime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class BiliBiliService {

    private final RestTemplate restTemplate;

    public BiliBiliService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 获取播放详情
     *
     * @return 获取播放列表
     */
    public List<BiliAnime> getBiliAnimeList() {
        List<BiliAnime> animeList = new LinkedList<>();
        //接口最大条数10000条
        long pageSize = 10000;
        long hasNext = 1;
        long num = 1;
        while (hasNext != 0) {
            try {
                BgngumeResponse response = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=4&st=1&sort=0&page={num}&season_type=1&pagesize={pageSize}&type=1", BgngumeResponse.class, num, pageSize);
                if (response != null && response.getCode() == 0 && response.getData() != null && response.getData().getList() != null) {
                    AnimeData data = response.getData();
                    animeList.addAll(data.getList());
                    hasNext = data.getHasNext();
                    num = data.getNum() + 1;
                } else {
                    log.error("获取数据失败了");
                    break;
                }
            } catch (Exception exception) {
                hasNext = 0;
                log.error("本次同步任务失败！");
            }
        }
        log.info("从哔哩哔哩同步了{}部动漫的评分数据！！", animeList.size());
        return animeList;
    }
}
