package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.common.annotation.Email;
import chenyunlong.zhangli.common.config.properties.ZhangliProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 异步发送邮件
 *
 * @author stan
 * @date 2020/09/23
 */
@Aspect
@Component
public class EmailAspect {

    private final Logger log = LoggerFactory.getLogger(EmailAspect.class);
    private final ZhangliProperties zhangliProperties;


    public EmailAspect(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }


    @Pointcut("@annotation(chenyunlong.zhangli.common.annotation.Email)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        //获取方法签名
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        //获取方法注解
        Email annotation = method.getAnnotation(Email.class);

        Object[] args = point.getArgs();
        log.debug("发送邮件的位置：" + annotation.receiver());
        //创建一个邮件发送线程！
        EmailThread emailThread = new EmailThread(annotation.receiver(), annotation.object(), annotation.content());
        emailThread.start();
        return point.proceed(args);
    }

    private class EmailThread extends Thread {
        private final String receiver;
        private final String subject;
        private final String content;

        public EmailThread(String receiver, String subject, String content) {

            this.receiver = receiver;
            this.subject = subject;
            this.content = content;
        }

        @Override
        public void run() {
            super.run();
            log.info(receiver, subject, content);
            log.debug("发送一条邮件给指定账户" + zhangliProperties.getFile().getBaseUploadDir());
        }
    }
}
