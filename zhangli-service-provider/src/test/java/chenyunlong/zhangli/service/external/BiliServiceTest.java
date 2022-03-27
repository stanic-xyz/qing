package chenyunlong.zhangli.service.external;

import chenyunlong.zhangli.model.bilibili.BiliAnime;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.List;

class BiliServiceTest {

    private final BiliService biliService = new BiliService(new RestTemplate());

    @Test
    void getBiliAnimeList() {
        List<BiliAnime> biliAnimeList = biliService.getBiliAnimeList();
        biliAnimeList.forEach(System.out::println);
    }
}