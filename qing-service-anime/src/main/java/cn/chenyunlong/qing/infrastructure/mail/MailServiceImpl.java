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

import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Mail service implementation.
 *
 * @author ryanwang
 * @author johnniang
 * @date 2019-03-17
 */
@Slf4j
@Service
public class MailServiceImpl extends AbstractMailService {

    private final FreeMarkerConfigurer freeMarker;

    public MailServiceImpl(FreeMarkerConfigurer freeMarker, JavaMailSender mailSender, QingProperties qingProperties) {
        super(qingProperties, mailSender);
        this.freeMarker = freeMarker;
    }

    @Override
    public void sendTextMail(String to, String subject, String content) {
        sendMailTemplate(false, messageHelper -> {
            try {
                messageHelper.setSubject(subject);
                messageHelper.setTo(to);
                messageHelper.setText(content);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to set message subject, to or test!", e);
            }
        });
    }

    @Override
    public void sendTemplateMail(String to, String subject, Map<String, Object> content,
                                 String templateName) {
        sendMailTemplate(true, messageHelper -> {
            // build message content with freemarker
            try {
                Configuration configuration = freeMarker.getConfiguration();
                Template template = configuration.getTemplate(templateName);
                String contentResult = FreeMarkerTemplateUtils.processTemplateIntoString(template, content);
                messageHelper.setSubject(subject);
                messageHelper.setTo(to);
                messageHelper.setText(contentResult, true);
            } catch (IOException | TemplateException e) {
                throw new RuntimeException("Failed to convert template to html!", e);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to set message subject, to or test", e);
            }
        });
    }

    @Override
    public void sendAttachMail(String to, String subject, Map<String, Object> content,
                               String templateName, String attachFilePath) {
        sendMailTemplate(true, messageHelper -> {
            try {
                messageHelper.setSubject(subject);
                messageHelper.setTo(to);
                Path attachmentPath = Paths.get(attachFilePath);
                messageHelper.addAttachment(attachmentPath.getFileName().toString(),
                        attachmentPath.toFile());
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to set message subject, to or test", e);
            }
        });
    }

    @Override
    public void testConnection() {
        super.testConnection();
    }

}
