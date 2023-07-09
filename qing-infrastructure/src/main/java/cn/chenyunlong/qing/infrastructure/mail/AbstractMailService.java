/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.infrastructure.mail;

import cn.chenyunlong.common.exception.EmailException;
import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Abstract mail service.
 *
 * @author johnniang
 */
@Slf4j
public abstract class AbstractMailService implements MailService {

    private static final int DEFAULT_POOL_SIZE = 5;


    private final QingProperties qingProperties;

    private JavaMailSender mailSender;

    @Nullable
    private ExecutorService executorService;
    private String cachedFromName;

    protected AbstractMailService(QingProperties qingProperties, JavaMailSender mailSender) {
        this.qingProperties = qingProperties;
        this.mailSender = mailSender;
    }

    @NonNull
    public ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
        }
        return executorService;
    }

    public void setExecutorService(@Nullable ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Test connection with email server.
     */
    @Override
    public void testConnection() {
        MailSender javaMailSender = getMailSender();
        if (javaMailSender instanceof JavaMailSenderImpl mailSender) {
            try {
                mailSender.testConnection();
            } catch (MessagingException e) {
                throw new EmailException("无法连接到邮箱服务器，请检查邮箱配置.[" + e.getMessage() + "]", e);
            }
        }
    }

    /**
     * Send mail template.
     *
     * @param callback mime message callback.
     */
    protected void sendMailTemplate(@Nullable Consumer<MimeMessageHelper> callback) {
        if (callback == null) {
            log.info("Callback is null, skip to send email");
            return;
        }
        if (!qingProperties.isMailEnabled()) {
            // If disabled
            log.info("Email has been disabled by yourself, you can re-enable it through email settings" + " on admin page.");
            return;
        }

        // get mail sender
        JavaMailSender mailSender = getMailSender();

        // create mime message helper
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage());

        try {
            // set from-name
            messageHelper.setFrom(getFromAddress(mailSender));
            // handle message set separately
            callback.accept(messageHelper);

            // get mime message
            MimeMessage mimeMessage = messageHelper.getMimeMessage();
            // send email
            mailSender.send(mimeMessage);

            log.info("Sent an email to [{}] successfully, subject: [{}], sent date: [{}]", Arrays.toString(mimeMessage.getAllRecipients()), mimeMessage.getSubject(), mimeMessage.getSentDate());
        } catch (Exception e) {
            throw new EmailException("邮件发送失败，请检查 SMTP 服务配置是否正确", e);
        }
    }

    /**
     * Send mail template if executor service is enable.
     *
     * @param callback   callback message handler
     * @param tryToAsync if the send procedure should try to asynchronous
     */
    protected void sendMailTemplate(boolean tryToAsync, @Nullable Consumer<MimeMessageHelper> callback) {
        ExecutorService executorService = getExecutorService();
        if (tryToAsync) {
            // send mail asynchronously
            executorService.execute(() -> sendMailTemplate(callback));
        } else {
            // send mail synchronously
            sendMailTemplate(callback);
        }
    }

    /**
     * Get java mail sender.
     *
     * @return java mail sender
     */
    @NonNull
    private synchronized JavaMailSender getMailSender() {
//        if (this.mailSender == null) {
//            // create mail sender factory
//            MailSenderFactory mailSenderFactory = new MailSenderFactory();
//            // get mail sender
//            this.mailSender = mailSenderFactory.getMailSender(mailProperties);
//        }
        return this.mailSender;
    }

    /**
     * Get from-address.
     *
     * @param javaMailSender java mail sender.
     * @return from-name internet address
     * @throws UnsupportedEncodingException throws when you give a wrong character encoding
     */
    private synchronized InternetAddress getFromAddress(@NonNull JavaMailSender javaMailSender) throws UnsupportedEncodingException {
        Assert.notNull(javaMailSender, "Java mail sender must not be null");

        if (javaMailSender instanceof JavaMailSenderImpl mailSender) {
            // get user name(email)
            String username = mailSender.getUsername();

            // build internet address
            return new InternetAddress(username, this.cachedFromName, mailSender.getDefaultEncoding());
        }

        throw new UnsupportedOperationException("Unsupported java mail sender: " + javaMailSender.getClass().getName());
    }

    /**
     * Clear cached instance.
     */
    protected void clearCache() {
        this.mailSender = null;
        this.cachedFromName = null;
        log.debug("Cleared all mail caches");
    }

}