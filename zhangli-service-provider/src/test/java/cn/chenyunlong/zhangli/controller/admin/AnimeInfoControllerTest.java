package cn.chenyunlong.zhangli.controller.admin;

import cn.chenyunlong.zhangli.controller.BaseApiTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

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
