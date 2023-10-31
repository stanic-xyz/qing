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

package cn.chenyunlong.qing.infrastructure.aspect;

import cn.chenyunlong.qing.infrastructure.annotation.Email;
import java.lang.reflect.Method;
import net.bytebuddy.matcher.BooleanMatcher;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 异步发送邮件
 *
 * @author 陈云龙
 * @since 2020/09/23
 */
@Aspect
@Component
public class EmailAspect {

    private final Logger log = LoggerFactory.getLogger(EmailAspect.class);

    @Pointcut("@annotation(cn.chenyunlong.qing.infrastructure.annotation.Email)")
    public void pointcut() {
        // do nothing
    }

    /**
     * 切面
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        ElementMatcher<Object> test = new BooleanMatcher<>(false);

        ElementMatcher.Junction<Object> junction =
            ElementMatchers.any().or(ElementMatchers.is(false));

        //获取方法签名
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        //获取方法注解
        Email annotation = method.getAnnotation(Email.class);

        Object[] args = point.getArgs();
        log.debug("发送邮件的位置：" + annotation.receiver());
        //创建一个邮件发送线程！
        EmailThread emailThread =
            new EmailThread(annotation.receiver(), annotation.object(), annotation.content());
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
        }
    }
}
