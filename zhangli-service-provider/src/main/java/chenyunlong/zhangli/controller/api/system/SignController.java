package chenyunlong.zhangli.controller.api.system;

import chenyunlong.zhangli.model.entities.Sign;
import chenyunlong.zhangli.service.SignService;
import chenyunlong.zhangli.annotation.Log;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 签到服务
 *
 * @author Stan
 */
@RestController
@RequestMapping("sign")
public class SignController {

    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @Log(title = "获取签到状态")
    @GetMapping("get")
    public Integer sign(Integer userId) {
        return signService.getSignStatus(userId);
    }

    @Log(title = "钉一下")
    @PostMapping("ding")
    public Object dingyixia(@RequestParam("userId") Long userId) {

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
        return sign;
    }
}
