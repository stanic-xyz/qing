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
import cn.chenyunlong.qing.domain.anime.anime.AnimeInfo;
import cn.chenyunlong.qing.domain.anime.anime.mapper.mp.AnimeInfoMapper;
import cn.chenyunlong.qing.domain.anime.episode.Episode;
import cn.chenyunlong.qing.domain.anime.episode.mapper.AnimeEpisodeMapper;
import cn.chenyunlong.qing.domain.anime.playlist.ListEpisodeEntity;
import cn.chenyunlong.qing.domain.anime.playlist.Playlist;
import cn.chenyunlong.qing.domain.anime.playlist.mapper.AnimeListEpisodeMapper;
import cn.chenyunlong.qing.domain.anime.playlist.mapper.AnimePlaylistMapper;
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
        QueryWrapper<Episode> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("LIMIT 0,1");
        Episode episodeEntity = animeEpisodeMapper.selectOne(queryWrapper);
        if (episodeEntity == null) {
            episodeEntity = new Episode();
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
                Playlist playList = new Playlist();
                playList.setAnimeId(animeInfo.getId());
                playList.setDescription("测试列表" + i);
                playList.setName("播放列表" + i);
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
