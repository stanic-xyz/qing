/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeChatMessageService {
    private final Logger log = LoggerFactory.getLogger(WeChatMessageService.class);
    //构建一个队列
    private final static Map<String, Long> messages = new ConcurrentHashMap<String, Long>();

    /**
     * 查看消息是否重复了
     *
     * @param signature 消息签名
     * @return 消息是否重复
     */
    public boolean isDuplicated(String signature) {

        boolean isDuplicated = messages.containsKey(signature);
        log.debug("是否被处理：" + isDuplicated);
        if (!isDuplicated) {
            messages.put(signature, System.currentTimeMillis());
        }

        Long currentMills = System.currentTimeMillis();

        for (String key : messages.keySet()) {
            Long aLong = messages.get(key);
            if (currentMills - aLong > 10000) {
                messages.remove(key);
            }
        }
        return isDuplicated;
    }
}
