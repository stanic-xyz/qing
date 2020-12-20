package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.model.agefans.AgePlayInfoModel;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;

@Slf4j
class AnimeInfoServiceImplTest {


    @Test
    void getPlayDetail() throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        Request request = new Request.Builder()
                .url("https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1&r=1231231252")
                .build();
        Response response1 = client.newCall(request).execute();


        String url = "https://www.agefans.net/_getplay?aid=20120053&playindex=3&epindex=2&r=";
        String referUrl = "https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1&r=123123123";
        String cookie = "fikker-ebUR-LnSf=gv7ZifqFiXqnepunyf30wdJReFQl52IZ";
        String playUrl = "https://play.agefans.net/_getplay2?kp=4zPeWaqNhi20IUkB6rXFO2pAJ1otsSb%2FIzpjyjuYSXKly9zoxWCaSFno%2FSoM8yFblxl8IVjeTJ5eIaCbwj%2BgpYDwjV02ZDL8dxaEsoxzUR9hzjeC4pd38w%3D%3D";

        try {
            String requestUrl = url + new Random().nextDouble();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Host", "play.agefans.net");
            ResponseEntity<AgePlayInfoModel> response = restTemplate.exchange(
                    playUrl,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    AgePlayInfoModel.class);
            AgePlayInfoModel responseBody = response.getBody();

            log.info("获取地址成功了！");
            log.info("获取地址成功了！:" + responseBody);
        } catch (Exception exception) {
            log.error("获取信息错误", exception);
        }
    }
}