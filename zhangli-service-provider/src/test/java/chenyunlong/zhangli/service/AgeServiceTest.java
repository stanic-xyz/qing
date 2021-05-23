package chenyunlong.zhangli.service;

import chenyunlong.zhangli.service.external.AgeService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class AgeServiceTest {

    private AgeService ageService = new AgeService(new RestTemplate());

    @Test
    void getPlayDetail() {
        ageService.getPlayDetail();
    }
}