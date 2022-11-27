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

package cn.chenyunlong.qing.service.external;

import cn.chenyunlong.qing.domain.third.bilibili.model.BiliAnime;
import cn.chenyunlong.qing.domain.third.bilibili.service.BiliBiliService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

class BiliBiliServiceTest {

    private final BiliBiliService biliBiliService = new BiliBiliService(new RestTemplate());

    @Test
    void getBiliAnimeList() {
        List<BiliAnime> biliAnimeList = biliBiliService.getBiliAnimeList();
        biliAnimeList.forEach(System.out::println);
    }
}
