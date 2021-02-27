package chenyunlong.zhangli.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
class okHttpClientBuilderTest {

    @Test
    public void testSSHTest() throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1";
//        String url = "https://www.baidu.cn";
        String json = "{}";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Host", "www.agefans.net");
        headers.put("Referer", "https://www.agefans.net");
        headers.put("Cookie", "__cfduid=df3a792afd828c09d36d0d86d94d848111607796144; t1=1608421256583; k1=1608484612");
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build();
        try {

            Response response = OkHttpClientBuilder.buildOkHttpClient()
                    .hostnameVerifier((s, sslSession) -> true)
                    .build()
                    .newCall(request)
                    .execute();
            if (response.isSuccessful()) {
                System.out.println(response.toString());
                assert response.body() != null;
                System.out.println(response.body().string());
            }
        } catch (Exception exception) {
            System.out.println("请求失败了");
        }
    }
}