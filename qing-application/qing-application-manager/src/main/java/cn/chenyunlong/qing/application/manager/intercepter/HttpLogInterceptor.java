package cn.chenyunlong.qing.application.manager.intercepter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class HttpLogInterceptor implements HandlerInterceptor {

    private final ThreadLocal<Long> startTime;

    {
        startTime = new ThreadLocal<>();
    }

    /**
     * 预处理回调方法，实现处理器的预处理（如检查登陆），第三个参数为响应的处理器，自定义Controller
     * <p>
     * 返回值：
     * true表示继续流程（如调用下一个拦截器或处理器）
     * false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器
     * 此时我们需要通过response来产生响应；
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
        @Nonnull
        HttpServletResponse response,
        @Nonnull
        Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        // 记录请求日志
        log.info("用户访问地址:{}, 请求方式: {}，请求地址：{}", request.getRemoteAddr(),
            request.getMethod(), request.getServletPath());
        log.info("----------------请求处理开始----------------");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 在任何情况下都会对返回的请求做处理
     * <p>
     * 即在视图渲染完毕时回调，如性能监控中我们可以在此记录结束时间并输出消耗时间
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(
        @Nonnull
        HttpServletRequest request,
        @Nonnull
        HttpServletResponse response,
        @Nonnull
        Object handler, Exception ex) throws Exception {
        log.info("请求处理结束. 处理耗时: {} ms", System.currentTimeMillis() - startTime.get());
        startTime.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
