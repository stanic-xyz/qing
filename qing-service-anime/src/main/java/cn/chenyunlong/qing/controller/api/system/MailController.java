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


import cn.chenyunlong.qing.infrastructure.mail.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件控制器
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "邮件服务")
@Slf4j
@RestController
@RequestMapping("mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;


    /**
     * 发送邮件
     *
     * @param to      收件人邮箱
     * @param subject 主题
     * @param message 消息
     * @return {@link String}
     */
    @GetMapping("mailto")
    public String sendMail(String to, String subject, String message) {
        mailService.sendTextMail(to, subject, message);
        return "发送成功";
    }

    /**
     * 根据邮件模板发送邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param message 消息
     * @return {@link String}
     */
    @GetMapping("templateMailto")
    public String sendTemplateMail(String to, String subject, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("from", "1576302867@qq.com");
        mailService.sendTemplateMail(to, subject, data, "register.ftl");
        return "发送成功";
    }
}
