package chenyunlong.zhangli.controller.admin.api;

import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
class EpisodeControllerTest {

    @Autowired
    private EpisodeController episodeController;

    @Autowired
    private AnimeInfoMapper animeInfoMapper;

    @Test
    void add() {

        List<AnimeInfo> animeInfoList = animeInfoMapper.selectList(new QueryWrapper<>());

        for (AnimeInfo animeInfo : animeInfoList) {
            for (int i = 0; i < 10; i++) {
                AddEpisodeParam episodePara = new AddEpisodeParam();
                episodePara.setName("第" + i + "集：测试视频" + i);
                episodePara.setStatus(1);
                episodePara.setUploaderId((long) 1);
                episodePara.setUploadTime(LocalDateTime.now());
                episodePara.setAnimeId(animeInfo.getId());
                episodePara.setUrl1("http://localhost/media/test1.mp4");
                episodePara.setUrl1("http://localhost/media/test2.mp4");
                episodePara.setUrl1("http://localhost/media/test3.mp4");
                episodeController.add(episodePara);

            }
        }
    }
}