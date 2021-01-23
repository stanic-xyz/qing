package chenyunlong.zhangli.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
class AnimeInfoServiceImplTest {

    private final OkHttpClient httpClient = new OkHttpClient();

    @Test
    void getPlayDetail() {
        Random random = new Random();

        String url = "https://www.agefans.net/_getplay?aid=20200026&playindex=2&epindex=1";
        String referUrl = "https://www.agefans.net";
        StringBuilder cookie = new StringBuilder();
        String playUrl = "https://play.agefans.net/_getplay2?kp=4zPeWaqNhi20IUkB6rXFO2pAJ1otsSb%2FIzpjyjuYSXKly9zoxWCaSFno%2FSoM8yFblxl8IVjeTJ5eIaCbwj%2BgpYDwjV02ZDL8dxaEsoxzUR9hzjeC4pd38w%3D%3D";

        long out_time = 10;
        httpClient.newBuilder().connectTimeout(out_time, TimeUnit.SECONDS)
                .readTimeout(out_time, TimeUnit.SECONDS)
                .writeTimeout(out_time, TimeUnit.SECONDS)
                .build();

        String currentUrl = url + "&r=" + random.nextDouble();
        Headers headers = new Headers.Builder()
                .add("Cache-Control", "no-cache")
                .add("Cookie", cookie.toString())
                .add("Host", "www.agefans.net")
                .add("Referer", referUrl)
                .build();
        Request request = new Request.Builder()
                .url(currentUrl)
                .headers(headers)
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert response != null;
        if (response.isSuccessful()) {
            List<String> sessions = response.headers().values("Set-Cookie");
            sessions.stream().map(session -> session.substring(0, session.indexOf(";"))).forEach(s -> {
                cookie.append(";").append(s);
            });
        }
        System.out.println(cookie.toString());

        headers = new Headers.Builder()
                .add("Cache-Control", "no-cache")
                .add("Cookie", cookie.toString())
                .add("Host", "www.agefans.net")
                .add("Referer", referUrl)
                .build();
        request = new Request.Builder()
                .url(currentUrl)
                .headers(headers)
                .build();
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println(response);
                String string = response.body().string();
                System.out.println(string);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}