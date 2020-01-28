package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.annotation.Email;
import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class EmailAspect {

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

    @Getter
    @Setter
    @AllArgsConstructor
    private class EmailThread extends Thread {
        private String receiver;
        private String object;
        private String content;

        @Override
        public void run() {
            super.run();
            emailService.sendEmail("1576302867@qq.com", "这是主题", "text");
            log.debug("发送一条邮件给指定账户");
        }
    }
}
