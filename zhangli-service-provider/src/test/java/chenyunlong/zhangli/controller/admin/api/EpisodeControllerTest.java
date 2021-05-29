package chenyunlong.zhangli.controller.admin.api;

import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import chenyunlong.zhangli.model.params.AddEpisodeParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
class EpisodeControllerTest {

    @Autowired
    private EpisodeController episodeController;

    @Autowired
    private AnimeInfoMapper animeInfoMapper;

    @Autowired
    private AnimePlaylistMapper playlistMapper;

    @Test
    void add() {

        List<AnimeInfo> animeInfoList = animeInfoMapper.selectList(new QueryWrapper<>());

        for (AnimeInfo animeInfo : animeInfoList) {
            //添加三个测试目录
            for (int i = 0; i < 3; i++) {
                PlaylistEntity playList = new PlaylistEntity();
                playList.setAnimeId(animeInfo.getId());
                playList.setDescription("测试列表" + i);
                playList.setName("播放列表" + i);
                playList.setSearchValue(animeInfo.getName());
                playList.preCheck();
                playlistMapper.insert(playList);

                for (int j = 0; j < 10; j++) {
                    AddEpisodeParam episodePara = new AddEpisodeParam();
                    episodePara.setName("第" + j + "集：测试视频" + j);
                    episodePara.setPlaylistId(playList.getId());
                    episodePara.setStatus(1);
                    episodePara.setUploaderId((long) 1);
                    episodePara.setUploadTime(LocalDateTime.now());
                    episodePara.setAnimeId(animeInfo.getId());
                    episodePara.setUrl1("http://localhost/media/test1.mp4");
                    episodePara.setUrl2("http://localhost/media/test2.mp4");
                    episodePara.setUrl3("http://localhost/media/test3.mp4");
                    episodeController.add(episodePara);
                }
            }
        }
    }
}