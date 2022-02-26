package chenyunlong.zhangli.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import redis.embedded.RedisServer;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.ServerSocket;

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
    public static void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            int localPort = serverSocket.getLocalPort();
            serverSocket.close();

            redisServer = RedisServer.builder()
                    .port(localPort)
                    .setting("maxheap 128m")
                    .setting("bind localhost")
                    .build();
            redisServer.start();
            //因为RedisServer启动的端口无法预知（如果写死的话就容易有端口冲突），所以需要实现动态端口配置。
            System.setProperty("spring.redis.port", Integer.toString(localPort));
        } catch (IOException e) {
            log.error("发生了异常", e);
        }
    }

    @AfterAll
    static void close() {
        if (redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }
}
