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

package cn.chenyunlong.qing.feign.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Stan
 */
@FeignClient(
    name = "base-platform",
    url = "192.168.129.1:30103",
    fallback = BaseServiceFallBack.class
)
public interface BaseService {

    /**
     * 获取系统的基本信息
     *
     * @param appid       应用ID
     * @param accessToken 访问密钥
     * @param sysIDs      系统ID列表
     * @param schoolId    学校ID
     * @return 学校的基本信息
     */
    @GetMapping("BaseApi/Global/GetSubSystemsInfoBySchool")
    String getUserInfo(@RequestParam String appid,
                       @RequestParam String accessToken,
                       @RequestParam String sysIDs,
                       @RequestParam String schoolId);
}
