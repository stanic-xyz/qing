package chenyunlong.zhangli.controller.system;

import chenyunlong.zhangli.common.annotation.Log;
import chenyunlong.zhangli.model.entities.Sign;
import chenyunlong.zhangli.model.support.ApiResult;
import chenyunlong.zhangli.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Stan
 */
@RestController
@RequestMapping("sign")
public class SignController {

    @Autowired
    private SignService signService;

    @Log(title = "获取签到状态")
    @GetMapping("get")
    public ApiResult<Integer> sign(Integer userId) {
        return ApiResult.success(signService.getSignStatus(userId));
    }

    @Log(title = "钉一下")
    @PostMapping("ding")
    public ApiResult<Object> dingyixia(@RequestParam("userId") Long userId) {

        int signRecord = 0;
        LocalDate date = LocalDate.now();
        int day = date.getDayOfMonth();

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
