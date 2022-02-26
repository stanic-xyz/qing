package chenyunlong.zhangli.controller.admin;

import chenyunlong.zhangli.controller.BaseApiTest;
import chenyunlong.zhangli.controller.api.auth.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

@Slf4j
public class AnimeInfoControllerTest extends BaseApiTest {

    @Order(2)
    @Test
    public void testAnimeList2() {
        System.out.println("测试2");
    }

    @Order(1)
    @Test
    public void testAnimeList() {
        System.out.println("测试1");
    }
}
