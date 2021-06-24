package chenyunlong.zhangli.service.external;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class BiliServiceTest {

    private final BiliService biliService = new BiliService(new RestTemplate());

    @Test
    void getBiliAnimeList() {
        biliService.getBiliAnimeList();
    }
}