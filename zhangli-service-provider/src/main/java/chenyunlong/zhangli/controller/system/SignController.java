package chenyunlong.zhangli.controller.system;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.entities.Sign;
import chenyunlong.zhangli.model.vo.ApiResult;
import chenyunlong.zhangli.common.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Stan
 */
@RestController
@RequestMapping("sign")
public class SignController {

    @Autowired
    private SignService signService;

    @Log("获取签到状态")
    @GetMapping("get")
    public ApiResult<Integer> sign(Integer userId) {
        return ApiResult.success(signService.getSignStatus(userId));
    }

    @Log("钉一下")
    @PostMapping("ding")
    public ApiResult<Object> dingyixia(@RequestParam("userId") Long userId) {

        int signRecord = 0;
        Date date = new Date();
        int day = date.getDay();

        signRecord = signRecord << day;

        Sign sign = new Sign();
        sign.setUserId(userId);
        sign.setDateMonth(date);
        sign.setContinueSignMonth(5);
        sign.setMask(signRecord);
        signService.addSign(sign);
        return ApiResult.success();
    }
}
