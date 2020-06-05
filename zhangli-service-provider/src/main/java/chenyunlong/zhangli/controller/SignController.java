package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.entities.Sign;
import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.model.response.BaseResponse;
import chenyunlong.zhangli.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("sign")
public class SignController {

    @Autowired
    private SignService signService;

    @GetMapping("get")
    public BaseResponse sign(Integer userId) {
        return ResultUtil.success(signService.getSignStatus(userId));
    }

    @PostMapping("ding")
    public BaseResponse dingyixia(@RequestParam("userId") Long userId) {

        int signRecord = 0;
        Date date = new Date();
        int day = date.getDay();

        signRecord = signRecord << day;

        Sign sign = new Sign();
        sign.setUserId(userId);
        sign.setDateMonth(date);
        sign.setContinueSignMonth(5);
        sign.setMask(signRecord);
        signService.AddSign(sign);
        return ResultUtil.success(null);
    }
}