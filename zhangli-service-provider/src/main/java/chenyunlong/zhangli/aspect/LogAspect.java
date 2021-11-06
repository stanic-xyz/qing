package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.annotation.Log;
import chenyunlong.zhangli.core.enums.BusinessStatus;
import chenyunlong.zhangli.model.entities.sys.SysOperLog;
import chenyunlong.zhangli.utils.HttpContextUtil;
import chenyunlong.zhangli.utils.IpUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * 日志记录切面
 *
 * @author Stan
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    @Pointcut("@annotation(chenyunlong.zhangli.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object result;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IpUtils.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (log.isDebugEnabled()) {
            // 保存日志
            log.debug("IP:" + ip + "，处理时间：" + time + "!");
        }
        return result;
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "pointcut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            String currentUser = "stan";

            // *========数据库日志=========*//
            SysOperLog operaLog = new SysOperLog();
            operaLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = "192.168.129.1";
            String deptName = "DeptName";
            String operUrl = "/test";

            operaLog.setOperIp(ip);
            // 返回参数
            if (!ObjectUtils.isEmpty(jsonResult)) {
                operaLog.setJsonResult(JSONObject.toJSONString(jsonResult));
            }

            operaLog.setOperUrl(operUrl);
            operaLog.setOperName(currentUser);
            operaLog.setDeptName(deptName);

            if (e != null) {
                operaLog.setStatus(BusinessStatus.FAIL.ordinal());
                //TODO 限制长度
                operaLog.setErrorMsg(e.getMessage());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operaLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operaLog.setRequestMethod(HttpContextUtil.getHttpServletRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operaLog);
            //TODO 保存数据库，暂时没有实现该功能
//            AsyncManager.me().execute(AsyncFactory.recordOper(operaLog));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log      日志
     * @param operaLog 操作日志
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operaLog) {
        // 设置action动作
        operaLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operaLog.setTitle(log.title());
        // 设置操作人类别
        operaLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operaLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog) {
        Map<String, String[]> map = HttpContextUtil.getHttpServletRequest().getParameterMap();
        if (!map.isEmpty()) {
            String params = JSONObject.toJSONString(map, excludePropertyPreFilter());
            operLog.setOperParam(params);
        } else {
            Object args = joinPoint.getArgs();
            if (args != null) {
                String params = argsArrayToString(joinPoint.getArgs());
                operLog.setOperParam(params);
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreFilters.MySimplePropertyPreFilter excludePropertyPreFilter() {
        return new PropertyPreFilters().addFilter().addExcludes(EXCLUDE_PROPERTIES);
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!isFilterObject(o)) {
                    Object jsonObj = JSONObject.toJSONString(o, excludePropertyPreFilter());
                    params.append(jsonObj).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
