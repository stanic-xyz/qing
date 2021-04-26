package chenyunlong.zhangli.task;


import chenyunlong.zhangli.mapper.BiliAnimeMapper;
import chenyunlong.zhangli.model.bilibili.AnimeData;
import chenyunlong.zhangli.model.bilibili.BgngumeResponse;
import chenyunlong.zhangli.model.bilibili.BiliAnime;
import chenyunlong.zhangli.model.entities.bilibili.BiliAnimeInfoEntity;
import chenyunlong.zhangli.utils.BeanUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    private final RestTemplate restTemplate;
    private final BiliAnimeMapper biliAnimeMapper;

    public ScheduledTask(RestTemplate restTemplate, BiliAnimeMapper biliAnimeMapper) {
        this.restTemplate = restTemplate;
        this.biliAnimeMapper = biliAnimeMapper;
    }

    /**
     * 同步上网记录
     * (启动时同步一次，之后每两小时同步一次基础平台的记录）
     */
    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void syncNetworkLogs() {
        new Thread(() -> {
            List<BiliAnime> animeList = new LinkedList<>();
            long pageSize = 1500;
            long hasNext = 1;
            long num = 1;
            while (hasNext != 0) {
                String responseStr = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=4&st=1&sort=0&page={num}&season_type=1&pagesize={pageSize}0&type=1", String.class, num, pageSize);
                BgngumeResponse response = new Gson().fromJson(responseStr, BgngumeResponse.class);
                if (response != null && response.getCode() == 0 && response.getData() != null) {
                    AnimeData responseData = response.getData();
                    animeList.addAll(responseData.getList());
                    hasNext = responseData.getHasNext();
                    num = responseData.getNum() + 1;
                } else {
                    log.error(responseStr);
                }
            }
            log.info("从哔哩哔哩同步了{}部动漫的评分数据！！", animeList.size());

            animeList.stream().map(animeInfo
                    -> {
                BiliAnimeInfoEntity animeInfoEntity = BeanUtils.transformFrom(animeInfo, BiliAnimeInfoEntity.class);
                assert animeInfoEntity != null;
                animeInfoEntity.setScore(StringUtils.hasText(animeInfo.getOrder()) ? Double.parseDouble(animeInfo.getOrder().replace("分", "")) : 0);
                return animeInfoEntity;
            }).forEach(biliAnimeMapper::insert);
        }).start();
    }
}
