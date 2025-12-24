package cn.chenyunlong.qing.auth.application.eventhandlers;

import cn.chenyunlong.qing.auth.application.port.out.EmailMessage;
import cn.chenyunlong.qing.auth.application.port.out.EmailService;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.event.UserRegistered;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 用户注册事件处理器
 * <p>
 * 负责在用户注册后发送激活邮件
 */
@Slf4j
@Component
public class UserRegisteredEventHandler {

    private final EmailService emailService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public UserRegisteredEventHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * 处理用户注册事件 - 发送激活邮件
     * <p>
     * 使用 @TransactionalEventListener 确保在事务提交后执行
     * 使用 @Async 异步执行，避免阻塞主线程
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegisteredEvent(UserRegistered event) {
        User user = event.user();
        log.info("开始处理用户注册事件，准备发送激活邮件。用户ID: {}, 邮箱: {}",
                user.getId().id(), user.getEmail().value());

        try {
            EmailMessage emailMessage = prepareActivationEmail(user);
            emailService.send(emailMessage);
            log.info("激活邮件发送请求已提交，用户ID: {}", user.getId().id());
        } catch (Exception e) {
            log.error("发送激活邮件失败，用户ID: {}", user.getId().id(), e);
            // 这里可以添加重试机制或死信队列处理
        }
    }

    private EmailMessage prepareActivationEmail(User user) {
        String activationLink = String.format("%s/api/v1/auth?code=%s",
                baseUrl, user.getActivationCode());

        String content = getString(user, activationLink);

        return EmailMessage.builder()
                .to(user.getEmail().value())
                .subject("账户激活 - 请完成您的注册")
                .content(content)
                .isHtml(true)
                .build();
    }

    private static @NonNull String getString(User user, String activationLink) {
        String username = user.getUsername().value();

        // 简单构建HTML内容，实际项目中建议使用模板引擎
        return String.format("""
                <html>
                <body>
                    <h2>欢迎注册，%s！</h2>
                    <p>请点击以下链接激活您的账户：</p>
                    <p><a href="%s">%s</a></p>
                    <p>如果链接无法点击，请复制并粘贴到浏览器地址栏中。</p>
                    <p>激活码有效期为24小时。</p>
                </body>
                </html>
                """, username, activationLink, activationLink);
    }
}
