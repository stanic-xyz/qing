package cn.chenyunlong.qing.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class HttpLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求日志
        log.info("用户访问地址:{}, 请求方式: {}，请求地址：{}", request.getRemoteAddr(), request.getMethod(), request.getServletPath());
        log.info("----------------请求头.start.....");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
