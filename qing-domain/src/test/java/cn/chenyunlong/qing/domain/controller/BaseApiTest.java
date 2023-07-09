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

package cn.chenyunlong.qing.domain.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base api test.
 *
 * @author stanChen
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BaseApiTest {

    private static RedisServer redisServer;

    @Resource
    protected MockMvc mockMvc;

    @BeforeAll
    static void baseSetUp() {
        System.out.println("在所有测试之前执行");
    }

    @BeforeEach
    void installBlog() {
        System.out.println("在每个测试开始之前执行");
    }


    @BeforeAll
    protected static void init() {
//        try {
//            ServerSocket serverSocket = new ServerSocket(0);
//            int localPort = serverSocket.getLocalPort();
//            serverSocket.close();
//
//            redisServer =
//                    RedisServer.builder().port(localPort).setting("maxheap 128m").setting("bind localhost").build();
//            redisServer.start();
//            //因为RedisServer启动的端口无法预知（如果写死的话就容易有端口冲突），所以需要实现动态端口配置。
//            System.setProperty("spring.redis.port", Integer.toString(localPort));
//        } catch (IOException e) {
//            log.error("发生了异常", e);
//        }
    }

    @AfterAll
    static void close() {
//        if (redisServer != null && redisServer.isActive()) {
//            redisServer.stop();
//        }
    }
}
