package cn.chenyunlong.qing.auth.interfaces.interceptor;//package cn.chenyunlong.qing.auth.interfaces.interceptor;
//
//import cn.chenyunlong.qing.auth.interfaces.config.RateLimitConfig;
//import cn.chenyunlong.qing.auth.interfaces.exception.ErrorResponse;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.concurrent.TimeUnit;
//
/// **
// * Request rate limit interceptor.
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@EnableConfigurationProperties(RateLimitConfig.RateLimitProperties.class)
//public class RateLimitInterceptor implements HandlerInterceptor {
//
//    private final RateLimitConfig.RateLimitService rateLimitService;
//    private final RateLimitConfig.SemaphoreRateLimiter semaphoreRateLimiter;
//    private final RateLimitConfig.RateLimitProperties rateLimitProperties;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (!rateLimitProperties.isEnabled()) {
//            return true;
//        }
//
//        String requestURI = request.getRequestURI();
//        String clientIp = getClientIp(request);
//
//        if (!checkGlobalConcurrentLimit(requestURI, response)) {
//            return false;
//        }
//        if (!checkIpRateLimit(clientIp, response)) {
//            return false;
//        }
//        if (!checkUserRateLimit(request, response)) {
//            return false;
//        }
//        if (!checkApiRateLimit(requestURI, clientIp, response)) {
//            return false;
//        }
//        if (!checkGlobalRateLimit(response)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        if (rateLimitProperties.isEnabled() && rateLimitProperties.getGlobal().isEnabled()) {
//            semaphoreRateLimiter.release("global_concurrent");
//        }
//    }
//
//    private boolean checkGlobalConcurrentLimit(String requestURI, HttpServletResponse response) throws IOException {
//        RateLimitConfig.RateLimitProperties.GlobalRateLimit globalConfig = rateLimitProperties.getGlobal();
//        if (!globalConfig.isEnabled()) {
//            return true;
//        }
//
//        boolean acquired = semaphoreRateLimiter.tryAcquire("global_concurrent",
//                globalConfig.getConcurrentLimit(), 100, TimeUnit.MILLISECONDS);
//
//        if (!acquired) {
//            log.warn("Global concurrent limit hit: URI={}, limit={}", requestURI, globalConfig.getConcurrentLimit());
//            writeRateLimitResponse(response, "System busy, please retry later", "GLOBAL_CONCURRENT_LIMIT");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean checkIpRateLimit(String clientIp, HttpServletResponse response) throws IOException {
//        RateLimitConfig.RateLimitProperties.IpRateLimit ipConfig = rateLimitProperties.getIp();
//        if (!ipConfig.isEnabled()) {
//            return true;
//        }
//
//        String key = rateLimitService.generateKey("ip", clientIp);
//        RateLimitConfig.RateLimitResult result = rateLimitService.isAllowed(
//                key, ipConfig.getLimit(), ipConfig.getWindowSeconds());
//
//        if (!result.isAllowed()) {
//            log.warn("IP rate limit hit: ip={}, current={}, limit={}, remainingSeconds={}",
//                    clientIp, result.getCurrent(), result.getLimit(), result.getRemainingTime());
//            writeRateLimitResponse(response,
//                    String.format("IP too frequent, retry in %d seconds", result.getRemainingTime()),
//                    "IP_RATE_LIMIT");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean checkUserRateLimit(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        RateLimitConfig.RateLimitProperties.UserRateLimit userConfig = rateLimitProperties.getUser();
//        if (!userConfig.isEnabled()) {
//            return true;
//        }
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() ||
//                "anonymousUser".equals(authentication.getPrincipal())) {
//            return true; // unauthenticated users skip user rate limit
//        }
//
//        String username = authentication.getName();
//        String key = rateLimitService.generateKey("user", username);
//        RateLimitConfig.RateLimitResult result = rateLimitService.isAllowed(
//                key, userConfig.getLimit(), userConfig.getWindowSeconds());
//
//        if (!result.isAllowed()) {
//            log.warn("User rate limit hit: user={}, current={}, limit={}, remainingSeconds={}",
//                    username, result.getCurrent(), result.getLimit(), result.getRemainingTime());
//            writeRateLimitResponse(response,
//                    String.format("User too frequent, retry in %d seconds", result.getRemainingTime()),
//                    "USER_RATE_LIMIT");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean checkApiRateLimit(String requestURI, String clientIp, HttpServletResponse response) throws IOException {
//        RateLimitConfig.RateLimitProperties.ApiRateLimit apiConfig = rateLimitProperties.getApi();
//        if (!apiConfig.isEnabled()) {
//            return true;
//        }
//
//        int limit = 0;
//        String apiType = "";
//        if (requestURI.contains("/login")) {
//            limit = apiConfig.getLoginLimit();
//            apiType = "login";
//        } else if (requestURI.contains("/register")) {
//            limit = apiConfig.getRegisterLimit();
//            apiType = "register";
//        } else {
//            return true; // other APIs skip special rate limit
//        }
//
//        String key = rateLimitService.generateKey("api_" + apiType, clientIp);
//        RateLimitConfig.RateLimitResult result = rateLimitService.isAllowedSlidingWindow(
//                key, limit, apiConfig.getWindowSeconds());
//
//        if (!result.isAllowed()) {
//            log.warn("API rate limit hit: api={}, ip={}, current={}, limit={}", apiType, clientIp, result.getCurrent(), result.getLimit());
//            writeRateLimitResponse(response,
//                    String.format("%s too frequent, please retry later", getApiDisplayName(apiType)),
//                    "API_RATE_LIMIT");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean checkGlobalRateLimit(HttpServletResponse response) throws IOException {
//        RateLimitConfig.RateLimitProperties.GlobalRateLimit globalConfig = rateLimitProperties.getGlobal();
//        if (!globalConfig.isEnabled()) {
//            return true;
//        }
//
//        String key = rateLimitService.generateKey("global", "all");
//        RateLimitConfig.RateLimitResult result = rateLimitService.isAllowed(
//                key, globalConfig.getLimit(), globalConfig.getWindowSeconds());
//
//        if (!result.isAllowed()) {
//            log.warn("Global rate limit hit: current={}, limit={}, remainingSeconds={}",
//                    result.getCurrent(), result.getLimit(), result.getRemainingTime());
//            writeRateLimitResponse(response, "System too frequent, please retry later", "GLOBAL_RATE_LIMIT");
//            return false;
//        }
//        return true;
//    }
//
//    private String getClientIp(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//            int index = ip.indexOf(',');
//            return index != -1 ? ip.substring(0, index) : ip;
//        }
//        ip = request.getHeader("X-Real-IP");
//        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
//        ip = request.getHeader("Proxy-Client-IP");
//        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
//        ip = request.getHeader("WL-Proxy-Client-IP");
//        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
//        ip = request.getHeader("HTTP_CLIENT_IP");
//        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
//        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
//            return ip;
//        }
//        return request.getRemoteAddr();
//    }
//
//    private String getApiDisplayName(String apiType) {
//        switch (apiType) {
//            case "login":
//                return "Login";
//            case "register":
//                return "Register";
//            default:
//                return apiType;
//        }
//    }
//
//    private void writeRateLimitResponse(HttpServletResponse response, String message, String code) throws IOException {
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.TOO_MANY_REQUESTS.value())
//                .error("Rate Limit Exceeded")
//                .message(message)
//                .code(code)
//                .build();
//        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(JSONUtil.toJsonStr(errorResponse));
//    }
//}
