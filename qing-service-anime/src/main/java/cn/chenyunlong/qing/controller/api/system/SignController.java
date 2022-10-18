/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.controller.api.system;

import cn.chenyunlong.qing.annotation.Log;
import cn.chenyunlong.qing.model.entities.Sign;
import cn.chenyunlong.qing.service.SignService;
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
