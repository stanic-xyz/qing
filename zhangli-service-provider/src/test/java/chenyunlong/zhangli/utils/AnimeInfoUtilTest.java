package chenyunlong.zhangli.utils;


import chenyunlong.zhangli.model.bilibili.AnimeData;
import chenyunlong.zhangli.model.bilibili.BgngumeResponse;
import chenyunlong.zhangli.model.bilibili.BiliAnime;
import chenyunlong.zhangli.model.dto.bilibili.AnimeInfoDTO;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.LinkedList;
import java.util.List;

@Slf4j
class AnimeInfoUtilTest {

    @Test
    public void testLocalDate() {

        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, (x509Certificates, s) -> true).build();
        } catch (Exception e) {
            log.error("初始化错误", e);
        }
        assert sslContext != null;
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1.2"},
                null,
                NoopHostnameVerifier.INSTANCE);
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setSSLSocketFactory(csf)
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);

        List<BiliAnime> animeList = new LinkedList<>();
        long pageSize = 20;
        long total = 0;
        long hasNext = 1;
        long num = 1;
        while (hasNext != 0) {
            String bangumiInfo = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=4&st=1&sort=0&page={num}&season_type=1&pagesize={pageSize}0&type=1", String.class, num, pageSize);
            BgngumeResponse response = new Gson().fromJson(bangumiInfo, BgngumeResponse.class);
            if (response != null && response.getCode() == 0 && response.getData() != null) {
                AnimeData responseData = response.getData();
                animeList.addAll(responseData.getList());
                total = responseData.getTotal();
                hasNext = responseData.getHasNext();
                num = responseData.getNum() + 1;
            } else {
                System.out.println(new Gson().toJson(response));
            }
        }
        System.out.println(animeList.size());
        for (BiliAnime animeDate : animeList) {
            System.out.println(new Gson().toJson(animeDate));
        }
        assert animeList.size() == total;
    }

    @Test
    public void testStreamEmptyAverage() {
        double average = new LinkedList<Integer>().stream().mapToInt(Integer::intValue).average().orElse(0);
        assert average == 0;
    }
}
