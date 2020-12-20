package chenyunlong.zhangli.utils;

import okhttp3.*;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.HashMap;

class OKHttpClientBuilderTest {

    @Test
    public void testSSHTest() throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://www.agefans.net/_getplay?aid=20200101&playindex=3&epindex=1";
//        String url = "https://www.baidu.cn";
        String json = "{}";
        HashMap headers = new HashMap<>();
        headers.put("Host", "www.agefans.net");
        headers.put("Referer", "https://www.agefans.net");
        headers.put("Cookie", "__cfduid=df3a792afd828c09d36d0d86d94d848111607796144; t1=1608421256583; k1=1608484612");
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build();
        Response response = OKHttpClientBuilder.buildOKHttpClient()
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                })
                .build()
                .newCall(request)
                .execute();
//                .enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        System.out.println("onFailure()");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                        System.out.println("onResponse()");
//                    }
//
//                });
        System.out.printf("");
    }
}