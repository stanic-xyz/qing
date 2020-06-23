package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.utils.HttpContextUtil;
import chenyunlong.zhangli.utils.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LogAspect {
    private final Logger log = LoggerFactory.getLogger(LogAspect.class);

    private final ZhangliProperties zhangliProperties;

    public LogAspect(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

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
            log.debug("IP:" + ip + "，处理时间：" + time + "!");
        }
        return result;
    }
}
