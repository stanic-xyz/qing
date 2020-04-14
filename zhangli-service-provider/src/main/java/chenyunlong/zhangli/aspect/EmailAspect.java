package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.annotation.Email;
import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class EmailAspect {

    private Logger log = LoggerFactory.getLogger(EmailAspect.class);
    @Autowired
    private ZhangliProperties zhangliProperties;

    @Autowired
    private EmailService emailService;


    @Pointcut("@annotation(chenyunlong.zhangli.annotation.Email)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object Around(ProceedingJoinPoint point) throws Throwable {

        //获取方法签名
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        //获取方法注解
        Email annotation = method.getAnnotation(Email.class);

        Object[] args = point.getArgs();
        log.debug("发送邮件的位置：" + annotation.receiver());
        if (annotation != null) {
            //TODO 创建一个邮件发送线程！
            EmailThread emailThread = new EmailThread(annotation.receiver(), annotation.object(), annotation.content());
            emailThread.start();
        }
        Object object = point.proceed(args);
        return object;
    }

    private class EmailThread extends Thread {
        private String receiver;
        private String object;
        private String content;

        public EmailThread(String receiver, String object, String content) {

            this.receiver = receiver;
            this.object = object;
            this.content = content;
        }

        @Override
        public void run() {
            super.run();
            emailService.sendEmail("1576302867@qq.com", "这是主题", "text");
            log.debug("发送一条邮件给指定账户");
        }
    }
}
