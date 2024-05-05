package cn.chenyunlong.qing.application.manager.controller;

import lombok.Data;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CheckInController {

    @MutationMapping
    public void checkIn(
        @Argument("req")
        CheckInReq request) {

    }

    @Data
    private static class CheckInReq {

        private String plate;
        private String time;
    }
}
