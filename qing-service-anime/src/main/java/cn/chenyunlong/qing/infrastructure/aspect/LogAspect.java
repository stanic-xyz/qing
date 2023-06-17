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

import cn.chenyunlong.qing.domain.system.log.SysOperateLog;
import cn.chenyunlong.qing.infrastructure.annotation.Log;
import cn.chenyunlong.qing.infrastructure.enums.BusinessStatus;
import cn.chenyunlong.qing.infrastructure.utils.HttpContextUtil;
import cn.chenyunlong.qing.infrastructure.utils.IpUtils;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Pointcut("@annotation(cn.chenyunlong.qing.infrastructure.annotation.Log)")
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

    /**
     * 处理日志
     *
     * @param joinPoint  连接点
     * @param e          异常信息
     * @param jsonResult json结果
     */
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
            SysOperateLog operaLog = new SysOperateLog();
            operaLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = "192.168.129.1";
            String deptName = "DeptName";
            String operateUrl = "/test";

            operaLog.setOperIp(ip);
            // 返回参数
            if (!ObjectUtils.isEmpty(jsonResult)) {
                operaLog.setJsonResult(JSONObject.toJSONString(jsonResult));
            }

            operaLog.setOperUrl(operateUrl);
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
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperateLog operaLog) {
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
     * @param sysLog 操作日志
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperateLog sysLog) {
        Map<String, String[]> map = HttpContextUtil.getHttpServletRequest().getParameterMap();
        if (!map.isEmpty()) {
            String params = JSONObject.toJSONString(map);
            sysLog.setOperParam(params);
        } else {
            Object args = joinPoint.getArgs();
            if (args != null) {
                String params = argsArrayToString(joinPoint.getArgs());
                sysLog.setOperParam(params);
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     *
     * @param joinPoint 连接点
     * @return {@link Log}
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
     * 把参数拼接成字符串
     *
     * @param paramsArray 参数数组
     * @return {@link String}
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object obj : paramsArray) {
                if (!isFilterObject(obj)) {
                    Object jsonObj = JSONObject.toJSONString(obj);
                    params.append(jsonObj).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param obj 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object obj) {
        Class<?> objClass = obj.getClass();
        //数组
        if (objClass.isArray()) {
            return objClass.getComponentType().isAssignableFrom(MultipartFile.class);
        }
        //集合
        else if (Collection.class.isAssignableFrom(objClass)) {
            Collection collection = (Collection) obj;
            return collection.getClass().getComponentType().isAssignableFrom(MultipartFile.class);

        }
        //map集合
        else if (Map.class.isAssignableFrom(objClass)) {
            Map map = (Map) obj;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        //单独的对象
        return obj instanceof MultipartFile || obj instanceof HttpServletRequest || obj instanceof HttpServletResponse || obj instanceof BindingResult;
    }
}
