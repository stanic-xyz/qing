package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.model.ResultUtil;
import chenyunlong.zhangli.service.EmailService;
import chenyunlong.zhangli.model.response.BaseResponse;
import freemarker.template.TemplateException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("mail")
@RestController
public class MailController {


    @Autowired
    private EmailService emailService;
    @Autowired
    private RedissonClient redissonClient;


    /**
     * 发送普通邮件
     *
     * @param message
     * @param receiver
     * @return
     */
    @GetMapping("sendEmail")
    public BaseResponse sendEmail(@RequestParam String message, @RequestParam String receiver) {
        RLock lock = redissonClient.getLock("testLock");
        if (!lock.isLocked()) {
            lock.lock();
            emailService.sendEmail("1576302867@qq.com", "这是主题", "text");
            lock.unlock();
        }
        return ResultUtil.success("ok");
    }


    /**
     * 发送模板邮件
     *
     * @param to
     * @param subject
     * @param text
     * @return
     */
    @GetMapping("sendTemplateEmail")
    public BaseResponse sendTemplateEmail(@RequestParam String to, @RequestParam String
            subject, @RequestParam String text) {

        Map<String, String> map = new HashMap<>();
        map.put("from", "Stanic");
        try {
            emailService.sendTemplateEmail(to, subject, map);
            return ResultUtil.success("ok");
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
            return ResultUtil.fail("发送邮件失败");
        }
    }

    /**
     * 发送包含附件的邮件
     *
     * @param to
     * @param subject
     * @return
     */
    @PostMapping("sendAttacheMentEmail")
    public BaseResponse sendAttachmentEmail(@RequestParam String to, @RequestParam String subject,
                                            @RequestParam String text, @RequestParam MultipartFile file) throws IOException {

        try {
            emailService.sendAttachmentEmail(to, subject, text, file);
            return ResultUtil.success("ok");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResultUtil.fail("发送失败了");
        }
    }
}

