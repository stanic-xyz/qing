/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.qing.infrastructure.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Endpoint(id = "test")
public class PrometheusCustomMonitor implements InitializingBean {

    private final MeterRegistry registry;
    private Counter requestErrorCount;
    private Counter loginCount;
    private DistributionSummary logErrorCount;
    private AtomicInteger failCaseNum;

    public PrometheusCustomMonitor(MeterRegistry registry) {
        this.registry = registry;
    }

    public Counter getRequestErrorCount() {
        return requestErrorCount;
    }

    public Counter getLoginCount() {
        return loginCount;
    }

    public DistributionSummary getLogErrorCount() {
        return logErrorCount;
    }

    public AtomicInteger getFailCaseNum() {
        return failCaseNum;
    }

    @Override
    public void afterPropertiesSet() {
        requestErrorCount = registry.counter("requests_error_total", "status", "error");
        loginCount = registry.counter("order_request_count", "login", "qing-service-anime");
        logErrorCount = registry.summary("pay_amount_sum", "pay-amount", "qing-service-anime");
        failCaseNum = registry.gauge("fail_case_num", new AtomicInteger(0));
    }
}

