package cn.chenyunlong.qing.application.manager.controller;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CheckInController {

    private static final Logger log = LoggerFactory.getLogger(CheckInController.class);

    @MutationMapping
    public boolean checkIn(
        @Argument("req")
        CheckInReq request) {
        log.info("checkIn: {}", JSONUtil.toJsonPrettyStr(request));
        return true;
    }

    @Data
    private static class CheckInReq {

        private String plate;
        private String time;
    }
}
