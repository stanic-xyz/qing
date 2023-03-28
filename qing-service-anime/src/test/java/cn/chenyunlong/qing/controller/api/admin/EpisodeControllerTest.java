/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.controller.api.admin;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.controller.BaseApiTest;
import cn.chenyunlong.qing.domain.anime.episode.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.anime.episode.query.EpisodeQuery;
import cn.chenyunlong.qing.domain.anime.episode.service.IEpisodeService;
import cn.chenyunlong.qing.domain.anime.episode.vo.EpisodeVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

class EpisodeControllerTest extends BaseApiTest {

    @Autowired
    private IEpisodeService episodeService;


    @Test
    void add() {
        PageRequestWrapper<EpisodeQuery> queryWrapper = new PageRequestWrapper<>();
        queryWrapper.setPage(1);
        queryWrapper.setPageSize(20);
        Page<EpisodeVO> episodeEntity = episodeService.findByPage(queryWrapper);

        if (episodeEntity == null) {
            EpisodeCreator creator = new EpisodeCreator();
            creator.setName("测试播放视频");
            creator.setStatus(0);
            creator.setOrderNo(0);
            creator.setUploaderId(0L);
            creator.setUploaderName("admin");
            creator.setUrl("https://anime-1255705827.cos.ap-guangzhou.myqcloud.com/media/testvideo.mp4");
            episodeService.createEpisode(creator);
        }
    }
}
