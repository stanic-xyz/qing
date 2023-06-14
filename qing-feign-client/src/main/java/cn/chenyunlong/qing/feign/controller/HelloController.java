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

package cn.chenyunlong.qing.feign.controller;

import cn.chenyunlong.qing.feign.remote.BaseService;
import cn.chenyunlong.qing.feign.remote.RemoteHello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stan
 */
@Slf4j
@RestController
public class HelloController {

    private final RemoteHello remoteHello;

    private final BaseService baseService;

    @Value("${test}")
    private String test;

    public HelloController(RemoteHello remoteHello, BaseService baseService) {
        this.remoteHello = remoteHello;
        this.baseService = baseService;
    }

    @GetMapping("hello")
    public String hello(@RequestParam String name) {
        String value = remoteHello.getUserInfo(name);
        log.debug("来自远程的请求");
        return "this is hello world!" + test + ":" + value;
    }


    @GetMapping("getSchoolInfo")
    public String hello(@RequestParam String appId, String accessToken, String sysIds, String schoolId) {
        String value = baseService.getUserInfo(appId, accessToken, sysIds, schoolId);
        return "this is hello world!" + test + ":" + value;
    }

}
