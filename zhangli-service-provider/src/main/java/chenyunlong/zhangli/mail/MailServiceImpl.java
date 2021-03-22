package chenyunlong.zhangli.mail;

import chenyunlong.zhangli.config.properties.ZhangliProperties;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
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
    private final ZhangliProperties zhangliProperties;

    public MailServiceImpl(FreeMarkerConfigurer freeMarker, ZhangliProperties zhangliProperties) {
        super(zhangliProperties);
        this.freeMarker = freeMarker;
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public void sendTextMail(String to, String subject, String content) {
        sendMailTemplate(true, messageHelper -> {
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
                String contentResult = FreeMarkerTemplateUtils.processTemplateIntoString(template,
                        content);
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
