package chenyunlong.zhangli.service.impl;

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
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
class AnimeInfoServiceImplTest {

    private final OkHttpClient httpClient = new OkHttpClient();

    @Test
    void getPlayDetail() {

        String url = "https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1&r=1231231252";
        String referUrl = "https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1&r=1231231252";
        String cookie = "fikker-ebUR-LnSf=gv7ZifqFiXqnepunyf30wdJReFQl52IZ";
        String playUrl = "https://play.agefans.net/_getplay2?kp=4zPeWaqNhi20IUkB6rXFO2pAJ1otsSb%2FIzpjyjuYSXKly9zoxWCaSFno%2FSoM8yFblxl8IVjeTJ5eIaCbwj%2BgpYDwjV02ZDL8dxaEsoxzUR9hzjeC4pd38w%3D%3D";

        long out_time = 10;
        httpClient.newBuilder().connectTimeout(out_time, TimeUnit.SECONDS)
                .readTimeout(out_time, TimeUnit.SECONDS)
                .writeTimeout(out_time, TimeUnit.SECONDS)
                .build();

        Headers headers = new Headers.Builder()
                .add("Cache-Control", "no-cache")
                .add("Cookie", "__cfduid=d449fd572f59b735736119750c6d7a47b1608814784; t1=1608814920166; k1=1665679358")
                .add("Host", "www.agefans.net")
                .add("Referer", "https://www.agefans.net")
                .build();
        Request request = new Request.Builder()
                .url("https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1&r=1231231252")
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

            sessions.forEach(session -> {
                String s = session.substring(0, session.indexOf(";"));
                log.info("Session:{}", s);
            });
        }
    }
}