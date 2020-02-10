package chenyunlong.zhangli.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(from);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }


    public void sendTemplateEmail(String to, String subject, Map<String, String> params) throws MessagingException, IOException, TemplateException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(from);
        helper.setTo(to);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates/mail");

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("register.ftl"), params);

        helper.setSubject(subject);
        helper.setText(html, true);//重点，默认为false，显示原始html代码，无效果

        mailSender.send(mimeMessage);

    }

    public void sendAttachmentEmail(String to, String subject, String text, MultipartFile file) throws MessagingException {
        Map<String, String> attachmentMap = new HashMap<>();
        attachmentMap.put("附件", "file.txt的绝对路径");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //是否发送的邮件是富文本（附件，图片，html等）
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setFrom(from);
        messageHelper.setTo(to);

        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);//重点，默认为false，显示原始html代码，无效果
        File tempFile = new File("C:\\Users\\Administrator\\Pictures\\风景动漫壁纸\\003.jpg");
        if (tempFile.exists()) {
            messageHelper.addAttachment(Objects.requireNonNull(tempFile.getName()), tempFile);
            messageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }
        mailSender.send(mimeMessage);
    }
}
