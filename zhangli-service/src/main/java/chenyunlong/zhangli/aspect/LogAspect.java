package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.utils.HttpContextUtil;
import chenyunlong.zhangli.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Autowired
    private ZhangliProperties zhangliProperties;

    @Pointcut("@annotation(chenyunlong.zhangli.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object Around(ProceedingJoinPoint point) throws Throwable {

        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (zhangliProperties.isOpenAopLog()) {
            // 保存日志
            log.debug(ip + "" + time + "!");
        }
        return result;
    }
}
