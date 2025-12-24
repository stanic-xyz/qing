package cn.chenyunlong.qing.auth.infrastructure.email;

import cn.chenyunlong.qing.auth.application.port.out.EmailMessage;
import cn.chenyunlong.qing.auth.application.port.out.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 邮件服务适配器
 * <p>
 * 实现应用层的EmailService接口，负责具体的邮件发送逻辑
 * 目前仅打印日志模拟发送
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceAdapter implements EmailService {

    @Override
    public void send(EmailMessage message) {
        log.info("========== 开始发送邮件 ==========");
        log.info("To: {}", message.getTo());
        log.info("Subject: {}", message.getSubject());
        log.info("Content: {}", message.getContent());
        log.info("IsHtml: {}", message.isHtml());
        //        emailService.send(message);
        log.info("========== 邮件发送结束 ==========");
    }
}
