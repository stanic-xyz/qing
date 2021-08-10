package chenyunlong.zhangli.intercepter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <br>日志拦截器</br>
 *
 * @author fattyca1
 * @since 1.0
 */
@Slf4j
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
    // 用来记录接口执行时间的最小接收值
    private final long timeoutMs;

    public LoggingRequestInterceptor(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    @NonNull
    @Override
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, ClientHttpRequestExecution execution) throws IOException {

        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long cost = System.currentTimeMillis() - start;
        if (cost > timeoutMs) {
            log.warn("Request uri: [{}], Cost times: {}ms", request.getURI(), cost);
        }
        // 打印日志
        trace(request, body, response);
        return response;
    }

    private void trace(HttpRequest request, byte[] body, ClientHttpResponse response) throws IOException {
        // 记录日志
        String responseStr = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
        log.info("\n" + "URI             : {}, \n" +
                        "Method       : {}, \n" +
                        "Headers      : {}, \n" +
                        "Param         : {}, \n" +
                        "RespStatus  : {}, \n" +
                        "Response    : {}", request.getURI(),
                request.getMethod(), request.getHeaders(), new String(body, StandardCharsets.UTF_8), response.getStatusCode(), responseStr);
    }
}