package chenyunlong.zhangli.utils;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

class AnimeInfoServiceImplTest {

    private static final RestTemplate httpClient;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60 * 1000);
        requestFactory.setReadTimeout(60 * 1000);
        httpClient = new RestTemplate(requestFactory);
    }

    @Test
    void getPlayDetail() {
        Random random = new Random();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //60s
        requestFactory.setConnectTimeout(60 * 1000);
        requestFactory.setReadTimeout(60 * 1000);


        String url = "https://www.agefans.cc/_getplay?aid=20200026&playindex=2&epindex=1";
        String referUrl = "https://www.agefans.cc";
        StringBuilder cookie = new StringBuilder();
        String playUrl = "https://play.agefans.cc/_getplay2?kp=4zPeWaqNhi20IUkB6rXFO2pAJ1otsSb%2FIzpjyjuYSXKly9zoxWCaSFno%2FSoM8yFblxl8IVjeTJ5eIaCbwj%2BgpYDwjV02ZDL8dxaEsoxzUR9hzjeC4pd38w%3D%3D";

        String currentUrl = url + "&r=" + random.nextDouble();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        headers.add("Cookie", cookie.toString());
        headers.add("Host", "www.agefans.cc");
        headers.add("Referer", referUrl);

        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response;
        response = httpClient.exchange(url, HttpMethod.GET, stringHttpEntity, String.class);


        List<String> sessions = response.getHeaders().get("Set-Cookie");
        assert sessions != null;
        sessions.stream().map(session -> session.substring(0, session.indexOf(";"))).forEach(s -> {
            cookie.append(";").append(s);
        });

        System.out.println(cookie.toString());


        headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache");
        headers.add("Cookie", cookie.toString());
        headers.add("Host", "www.agefans.cc");
        headers.add("Referer", referUrl);
        stringHttpEntity = new HttpEntity<>(null, headers);

        response = httpClient.exchange(currentUrl, HttpMethod.GET, stringHttpEntity, String.class);

        System.out.println(response);
        String string = response.getBody();
        System.out.println(string);
    }
}
