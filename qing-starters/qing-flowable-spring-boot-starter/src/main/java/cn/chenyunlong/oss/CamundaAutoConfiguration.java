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

package cn.chenyunlong.oss;

import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
public class CamundaAutoConfiguration {

    @Resource
    private RuntimeService runtimeService;

    /**
     * 部署完成事件通知
     *
     * @param event 完成部署事件
     */
    @EventListener
    public void processPostDeploy(PostDeployEvent event) {
        String name = event.getProcessEngine().getName();
        log.info("流程示例部署完成：{}", name);
        runtimeService.startProcessInstanceByKey("loanApproval");

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("assigneeList030", Arrays.asList("kermit", "demo"));
        variables.put("assigneeList040", Arrays.asList("kermit", "demo"));
        variables.put("starter", "demo");
        variables.put("amount", "980");
        runtimeService.startProcessInstanceByKey("Process_Demo1", variables);
    }

    /**
     * Camunda CORS Filter in Spring Boot Application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
