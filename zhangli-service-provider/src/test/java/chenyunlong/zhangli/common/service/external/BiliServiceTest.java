package chenyunlong.zhangli.common.service.external;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class BiliServiceTest {

    private final BiliService biliService = new BiliService(new RestTemplate());

    @Test
    void getBiliAnimeList() {
        biliService.getBiliAnimeList();
    }
}