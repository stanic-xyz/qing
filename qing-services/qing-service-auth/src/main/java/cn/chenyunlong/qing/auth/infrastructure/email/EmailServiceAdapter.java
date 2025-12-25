package cn.chenyunlong.qing.auth.infrastructure.email;

import cn.chenyunlong.qing.auth.application.port.out.EmailMessage;
import cn.chenyunlong.qing.auth.application.port.out.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

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

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMail;

    @Override
    public void sendActivateEmail(EmailMessage message) throws MessagingException {
        log.info("========== 开始发送邮件 ==========");
        log.info("To: {}", message.getTo());
        log.info("Subject: {}", message.getSubject());
        log.info("Content: {}", message.getContent());
        log.info("IsHtml: {}", message.isHtml());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setTo(message.getTo());
        mimeMessageHelper.setFrom(sendMail);
        mimeMessageHelper.setTo(message.getTo());
        mimeMessageHelper.setSubject(message.getSubject());
        mimeMessageHelper.setText(message.getContent(), true);
        javaMailSender.send(mimeMessage);
        log.info("========== 邮件发送结束 ==========");
    }
}
