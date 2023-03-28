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

package cn.chenyunlong.qing.registry.controller;

import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 我的eureka服务控制器（查询一些简单服务信息）
 *
 * @author Stan
 * @date 2022/10/18
 */
@RestController
@RequestMapping("myEureka")
public class MyEurekaController {

    @Resource
    private PeerAwareInstanceRegistry instanceRegistry;
    @Resource
    private DiscoveryClient discoveryClient;

    /**
     * 得到所有服务器信息
     *
     * @return {@link Applications}
     */
    @GetMapping("listApplications")
    public Applications getAllServerInfo() {
        return instanceRegistry.getApplications();
    }

    /**
     * 得到所有服务id
     *
     * @return {@link List}<{@link String}>
     */
    @GetMapping("listServices")
    public List<String> getAllServiceIds() {
        return discoveryClient.getServices();
    }

    /**
     * 通过id获取服务
     *
     * @param serviceId 服务id
     * @return {@link List}<{@link ServiceInstance}>
     */
    @GetMapping("getServiceById")
    public List<ServiceInstance> getServiceById(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }
}
