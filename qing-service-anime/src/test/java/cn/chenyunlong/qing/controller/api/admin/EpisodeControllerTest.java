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

package cn.chenyunlong.qing.controller.api.admin;

import cn.chenyunlong.qing.controller.BaseApiTest;
import cn.chenyunlong.qing.domain.anime.AnimeEpisodeEntity;
import cn.chenyunlong.qing.domain.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.ListEpisodeEntity;
import cn.chenyunlong.qing.domain.anime.PlaylistEntity;
import cn.chenyunlong.qing.mapper.AnimeEpisodeMapper;
import cn.chenyunlong.qing.mapper.AnimeInfoMapper;
import cn.chenyunlong.qing.mapper.AnimeListEpisodeMapper;
import cn.chenyunlong.qing.mapper.AnimePlaylistMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

class EpisodeControllerTest extends BaseApiTest {

    @Autowired
    private AnimeEpisodeMapper animeEpisodeMapper;

    @Autowired
    private AnimeInfoMapper animeInfoMapper;

    @Autowired
    private AnimePlaylistMapper playlistMapper;

    @Autowired
    private AnimeListEpisodeMapper listEpisodeMapper;

    @Test
    void add() {
        List<AnimeInfo> animeInfoList = animeInfoMapper.selectList(new QueryWrapper<>());
        QueryWrapper<AnimeEpisodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("LIMIT 0,1");
        AnimeEpisodeEntity episodeEntity = animeEpisodeMapper.selectOne(queryWrapper);
        if (episodeEntity == null) {
            episodeEntity = new AnimeEpisodeEntity();
            episodeEntity.setName("测试播放视频");
            episodeEntity.setStatus(0);
            episodeEntity.setOrderNo(0);
            episodeEntity.setUploaderId(0L);
            episodeEntity.setUploaderName("admin");
            episodeEntity.setUrl("https://anime-1255705827.cos.ap-guangzhou.myqcloud.com/media/testvideo.mp4");
            animeEpisodeMapper.insert(episodeEntity);
        }

        Optional<AnimeInfo> optionalAnimeInfo = animeInfoList.stream().findFirst();

        if (optionalAnimeInfo.isPresent()) {
            AnimeInfo animeInfo = optionalAnimeInfo.get();
            //添加三个测试目录
            for (int i = 0; i < 3; i++) {
                PlaylistEntity playList = new PlaylistEntity();
                playList.setAnimeId(animeInfo.getId());
                playList.setDescription("测试列表" + i);
                playList.setName("播放列表" + i);
                playList.setSearchValue("");
                playList.preCheck();
                playlistMapper.insert(playList);

                ListEpisodeEntity listEpisodeEntiry = new ListEpisodeEntity();
                listEpisodeEntiry.setListId(playList.getId());
                listEpisodeEntiry.setEpisodeId(episodeEntity.getId());
                listEpisodeMapper.insert(listEpisodeEntiry);
            }
        }
    }
}
