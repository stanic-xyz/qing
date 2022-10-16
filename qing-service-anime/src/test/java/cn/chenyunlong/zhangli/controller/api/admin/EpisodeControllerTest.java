package cn.chenyunlong.zhangli.controller.api.admin;

import cn.chenyunlong.zhangli.controller.BaseApiTest;
import cn.chenyunlong.zhangli.mapper.AnimeEpisodeMapper;
import cn.chenyunlong.zhangli.mapper.AnimeInfoMapper;
import cn.chenyunlong.zhangli.mapper.AnimeListEpisodeMapper;
import cn.chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import cn.chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import cn.chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import cn.chenyunlong.zhangli.model.entities.anime.ListEpisodeEntity;
import cn.chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
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