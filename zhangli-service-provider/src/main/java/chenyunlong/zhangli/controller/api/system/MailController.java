package chenyunlong.zhangli.controller.api.system;


import chenyunlong.zhangli.mail.MailService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stan
 */
@Api(tags = "邮件服务")
@Slf4j
@RestController
@RequestMapping("mail")
public class MailController {

    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }


    @GetMapping("mailto")
    public String sendMail(String to, String subject, String message) {
        mailService.sendTextMail(to, subject, message);
        return "发送成功";
    }

    @GetMapping("templateMailto")
    public String sendTemplateMail(String to, String subject, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("from", "1576302867@qq.com");
        mailService.sendTemplateMail(to, subject, data, "register.ftl");
        return "发送成功";
    }
}
