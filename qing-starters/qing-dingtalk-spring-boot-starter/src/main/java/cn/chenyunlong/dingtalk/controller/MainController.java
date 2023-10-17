package cn.chenyunlong.dingtalk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 主页。
 *
 * @author Stan
 * @since 2022/10/2022/10/18
 */
@Slf4j
@Controller
public class MainController {

    @RequestMapping("/")
    public String index(@RequestParam(name = "corpId") String corpId,
                        @RequestParam(name = "openConversationId") String openConversationId) {
        log.info("index corpId={}, openConversationId={}.", corpId, openConversationId);
        return "index.html";
    }
}
