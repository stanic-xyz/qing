package cn.chenyunlong.qing.auth.application.port.out;


import jakarta.mail.MessagingException;

/**
 * 邮件服务端口
 * <p>
 * 定义邮件发送的接口，由基础设施层实现
 */
public interface EmailService {
    /**
     * 发送邮件
     *
     * @param message 邮件消息
     */
    void sendActivateEmail(EmailMessage message) throws MessagingException;
}
