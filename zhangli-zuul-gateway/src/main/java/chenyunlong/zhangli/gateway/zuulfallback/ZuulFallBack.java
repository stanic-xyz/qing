package chenyunlong.zhangli.gateway.zuulfallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class ZuulFallBack implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
            /**
             * 客户端向网关发送服务成功，网关向api服务请求失败，不应该把api的404 500 等问题抛给客户端
             * 网关和api服务对客户端来说都是黑盒子。
             * @return
             * @throws IOException
             */
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";

            }

            @Override
            public void close() {
            }

            /**
             * 微服务出现宕机后，客户端再次请求就会返回fallback中的预设值
             * @return
             * @throws IOException
             */

            @Override
            public InputStream getBody() throws IOException {
                //服务异常时，输出此处内容，并打印错误日志
                log.error("error appear ");
                return new ByteArrayInputStream("this service is ubable use".getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
