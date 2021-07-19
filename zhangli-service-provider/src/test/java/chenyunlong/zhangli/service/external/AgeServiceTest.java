package chenyunlong.zhangli.service.external;

import chenyunlong.zhangli.mapper.AnimeEpisodeMapper;
import chenyunlong.zhangli.mapper.AnimeInfoMapper;
import chenyunlong.zhangli.mapper.AnimeListEpisodeMapper;
import chenyunlong.zhangli.mapper.AnimePlaylistMapper;
import chenyunlong.zhangli.model.agefans.AgeAnimeEpisode;
import chenyunlong.zhangli.model.agefans.AgeAnimeInfo;
import chenyunlong.zhangli.model.agefans.AgePlayInfoModel;
import chenyunlong.zhangli.model.agefans.AgePlayList;
import chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.entities.anime.ListEpisodeEntity;
import chenyunlong.zhangli.model.entities.anime.PlaylistEntity;
import chenyunlong.zhangli.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
class AgeServiceTest {

    @Autowired
    private AnimeInfoMapper animeInfoMapper;

    @Autowired
    private AnimePlaylistMapper playlistMapper;

    @Autowired
    private AnimeListEpisodeMapper listEpisodeMapper;

    @Autowired
    private AnimeEpisodeMapper episodeMapper;

    private final AgeService ageService = new AgeService(new RestTemplate(), new ObjectMapper());

    @Test
    void getPlayDetail() {
        AgePlayInfoModel detail = ageService.getPlayDetail(20110063, 2, 1);
        System.out.println(detail);
        assertNotNull(detail);
        String virtualUrl = "https%3a%2f%2f1251316161%2evod2%2emyqcloud%2ecom%2f007a649dvodcq1251316161%2f440e8b4e5285890811966453306%2fZYeaYLq4UPoA%2emp4";
        assertEquals(virtualUrl, detail.getVurl());
    }

    @Test
    void getDetail() {
        AgeAnimeInfo ageAnimeInfo = ageService.getDetail(20150066);

        assertNotNull(ageAnimeInfo);

        assertEquals("Angel Beats! Hell's Kitchen", ageAnimeInfo.getName());
        assertEquals(3, ageAnimeInfo.getAgePlayListList().size());
    }


    @Test
    void syncAnimeInfo() {

        FileOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            File file = ResourceUtils.getFile("d://current.txt");
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    return;
                }
            }
            outputStream = new FileOutputStream(file, true);
            inputStream = new FileInputStream(file);

            LambdaQueryWrapper<AnimeInfo> queryWrapper = new LambdaQueryWrapper<>();
            //查询原有的动漫信息
            List<AnimeInfo> animeInfoList = animeInfoMapper.selectList(queryWrapper);
            byte[] bytes = new byte[inputStream.available()];

            int read = inputStream.read(bytes);
            String haRead = new String(bytes);

            List<String> readIds = Arrays.stream(StringUtils.split(haRead, ",")).collect(Collectors.toList());


            if (!animeInfoList.isEmpty()) {
                for (AnimeInfo animeInfo : animeInfoList) {
                    if (readIds.contains(animeInfo.getId().toString())) {
                        continue;
                    }
                    outputStream.write(animeInfo.getId().toString().getBytes(StandardCharsets.UTF_8));
                    outputStream.write(",".getBytes(StandardCharsets.UTF_8));
                    AgeAnimeInfo detail = ageService.getDetail(animeInfo.getId());
                    if (detail != null) {
                        animeInfo.setAuthor(detail.getAuthor());
                        animeInfo.setCompany(detail.getCompany());
                        animeInfo.setName(detail.getName());
                        animeInfo.setOfficialWebsite(detail.getOfficialWebsite());
                        animeInfo.setOtherName(detail.getOtherName());
                        animeInfo.setInstruction(detail.getInstruction());
                        animeInfo.setPlotType(detail.getPlotType());
                        animeInfo.setTags(detail.getTags());
                        animeInfo.setDistrict(detail.getDistrict());
                        animeInfo.setType(detail.getType());
                        animeInfo.setCreateTime(LocalDateTime.now());
                        animeInfo.setPremiereDate(detail.getPremiereDate());
                        animeInfo.setPlayStatus(detail.getPlayStatus());
                        animeInfoMapper.updateById(animeInfo);
                    }

                    assert detail != null;
                    List<AgePlayList> playLists = detail.getAgePlayListList();

                    for (AgePlayList playList : playLists) {
                        PlaylistEntity playListEntity = new PlaylistEntity();
                        playListEntity.setAnimeId(animeInfo.getId());
                        playListEntity.setName(String.format("播放列表%d", playList.getIndex()));
                        playListEntity.setDescription(String.format("播放列表%d", playList.getIndex()));
                        //添加播放列表
                        int insert = playlistMapper.insert(playListEntity);

                        if (insert > 0) {
                            List<AgeAnimeEpisode> episodeList = playList.getEpisodeList();
                            for (AgeAnimeEpisode ageEpisode : episodeList) {
                                //插入播放单集信息
                                AnimeEpisodeEntity episode = new AnimeEpisodeEntity();
                                episode.setUploaderName("admin");
                                episode.setName(ageEpisode.getTitle());
                                episode.setStatus(1);
                                episode.setCreateTime(LocalDateTime.now());
                                episode.setSearchValue(ageEpisode.getHref());
                                episode.setUrl("https://1251316161.vod2.myqcloud.com/007a649dvodcq1251316161/75b32a815285890809903778875/5cAXY7aMUEQA.mp4");
                                episode.setOrderNo(0);
                                episode.setCreateBy("admin");
                                episodeMapper.insert(episode);

                                ListEpisodeEntity listEpisode = new ListEpisodeEntity();
                                listEpisode.setEpisodeId(episode.getId());
                                listEpisode.setListId(playListEntity.getId());
                                listEpisode.setCreateTime(LocalDateTime.now());
                                listEpisode.setUpdateTime(LocalDateTime.now());
                                listEpisodeMapper.insert(listEpisode);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }
}