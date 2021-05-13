package chenyunlong.zhangli.task;


import chenyunlong.zhangli.model.bilibili.AnimeData;
import chenyunlong.zhangli.model.bilibili.BgngumeResponse;
import chenyunlong.zhangli.model.bilibili.BiliAnime;
import chenyunlong.zhangli.service.BilibiliAnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;


@Slf4j
@Component
@EnableScheduling
public class ScheduledTask {

    private final RestTemplate restTemplate;
    private final BilibiliAnimeService bilibiliAnimeService;
    private final Executor executor;

    public ScheduledTask(RestTemplate restTemplate, BilibiliAnimeService bilibiliAnimeService, Executor taskExecutor) {
        this.restTemplate = restTemplate;
        this.bilibiliAnimeService = bilibiliAnimeService;
        this.executor = taskExecutor;
    }

    /**
     * 同步上网记录
     * (启动时同步一次，之后每两小时同步一次基础平台的记录）
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void syncNetworkLogs() {
        executor.execute(() -> {
            List<BiliAnime> animeList = new LinkedList<>();
            //接口最大条数10000条
            long pageSize = 10000;
            long hasNext = 1;
            long num = 1;
            while (hasNext != 0) {
                BgngumeResponse response = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=4&st=1&sort=0&page={num}&season_type=1&pagesize={pageSize}&type=1", BgngumeResponse.class, num, pageSize);
                if (response != null && response.getCode() == 0 && response.getData() != null && response.getData().getList() != null) {
                    AnimeData data = response.getData();
                    animeList.addAll(data.getList());
                    hasNext = data.getHasNext();
                    num = data.getNum() + 1;
                } else {
                    log.error("获取数据失败了");
                    break;
                }
            }
            log.info("从哔哩哔哩同步了{}部动漫的评分数据！！", animeList.size());

            bilibiliAnimeService.insertBatch(animeList);
        });
    }
}
