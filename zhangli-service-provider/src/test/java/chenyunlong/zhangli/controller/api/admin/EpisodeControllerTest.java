package chenyunlong.zhangli.controller.api.admin;

import chenyunlong.zhangli.controller.BaseApiTest;
import chenyunlong.zhangli.mapper.AnimeEpisodeMapper;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.mapper.AnimeListEpisodeMapper;
import chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.entities.anime.ListEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    @Disabled
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

        for (AnimeInfo animeInfo : animeInfoList) {
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