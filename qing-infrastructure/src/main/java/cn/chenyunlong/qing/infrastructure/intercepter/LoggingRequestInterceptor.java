/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.infrastructure.intercepter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

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
    public ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

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

    private void trace(HttpRequest request, byte[] body, ClientHttpResponse response)
        throws IOException {
        // 记录日志
        String responseStr = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
        log.info(new StringBuilder()
                .append("\n")
                .append("URI             : {}, \n")
                .append("Method       : {}, \n")
                .append("Headers      : {}, \n")
                .append("Param         : {}, \n")
                .append("RespStatus  : {}, \n")
                .append("Response    : {}")
                .toString(), request.getURI(), request.getMethod(), request.getHeaders(),
            new String(body, StandardCharsets.UTF_8), response.getStatusCode(), responseStr);
    }
}
