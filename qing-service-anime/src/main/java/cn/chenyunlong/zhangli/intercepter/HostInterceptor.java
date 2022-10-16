package cn.chenyunlong.zhangli.intercepter;

import cn.chenyunlong.zhangli.config.properties.ZhangliProperties;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 添加的cdn服务器
 *
 * @author Stan
 */
public class HostInterceptor implements HandlerInterceptor {

    private final ZhangliProperties zhangliProperties;

    public HostInterceptor(ZhangliProperties zhangliProperties) {
        this.zhangliProperties = zhangliProperties;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) throws Exception {
        //添加host过滤器
        if (modelAndView != null) {
            modelAndView.addObject("hostname", zhangliProperties.getFile().getImageServerUrl());
        }
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {

    }
}