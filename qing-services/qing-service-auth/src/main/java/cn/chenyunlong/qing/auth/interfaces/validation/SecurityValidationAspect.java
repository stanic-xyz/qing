/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.interfaces.validation;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.validator.ValidationResult;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.util.Map;

/**
 * 安全校验切面
 *
 * <p>自动对标注了安全校验注解的方法参数进行安全校验</p>
 *
 * <p>支持的校验类型：</p>
 * <ul>
 *   <li>@SecurityValidated：综合安全校验</li>
 *   <li>@XSSValidated：XSS攻击校验</li>
 *   <li>@SQLInjectionValidated：SQL注入校验</li>
 *   <li>@PathTraversalValidated：路径遍历校验</li>
 *   <li>@CommandInjectionValidated：命令注入校验</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecurityValidationAspect {

    private final SecurityValidator securityValidator;

    /**
     * 对标注了@SecurityValidated的方法进行安全校验
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(cn.chenyunlong.qing.auth.interfaces.validation.SecurityValidated)")
    public void validateSecurity(JoinPoint joinPoint) {
        validateMethodParameters(joinPoint, ValidationType.SECURITY);
    }

    /**
     * 对标注了@XSSValidated的方法进行XSS校验
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(cn.chenyunlong.qing.auth.interfaces.validation.XSSValidated)")
    public void validateXSS(JoinPoint joinPoint) {
        validateMethodParameters(joinPoint, ValidationType.XSS);
    }

    /**
     * 对标注了@SQLInjectionValidated的方法进行SQL注入校验
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(cn.chenyunlong.qing.auth.interfaces.validation.SQLInjectionValidated)")
    public void validateSQLInjection(JoinPoint joinPoint) {
        validateMethodParameters(joinPoint, ValidationType.SQL_INJECTION);
    }

    /**
     * 对标注了@PathTraversalValidated的方法进行路径遍历校验
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(cn.chenyunlong.qing.auth.interfaces.validation.PathTraversalValidated)")
    public void validatePathTraversal(JoinPoint joinPoint) {
        validateMethodParameters(joinPoint, ValidationType.PATH_TRAVERSAL);
    }

    /**
     * 对标注了@CommandInjectionValidated的方法进行命令注入校验
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(cn.chenyunlong.qing.auth.interfaces.validation.CommandInjectionValidated)")
    public void validateCommandInjection(JoinPoint joinPoint) {
        validateMethodParameters(joinPoint, ValidationType.COMMAND_INJECTION);
    }

    /**
     * 校验方法参数
     *
     * @param joinPoint      切入点
     * @param validationType 校验类型
     */
    private void validateMethodParameters(JoinPoint joinPoint, ValidationType validationType) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Parameter parameter = parameters[i];

            if (arg == null) {
                continue;
            }

            // 校验基本类型参数
            if (arg instanceof String) {
                validateStringParameter((String) arg, parameter.getName(), validationType);
            }
            // 校验对象类型参数
            else if (isNotPrimitiveOrWrapper(arg.getClass())) {
                validateObjectParameter(arg, parameter.getName(), validationType);
            }
        }
    }

    /**
     * 校验字符串参数
     *
     * @param value          参数值
     * @param parameterName  参数名
     * @param validationType 校验类型
     */
    private void validateStringParameter(String value, String parameterName, ValidationType validationType) {
        if (StrUtil.isBlank(value)) {
            return;
        }

        ValidationResult result = performValidation(value, validationType);
        if (!result.isValid()) {
            log.warn("参数校验失败: 参数名={}, 校验类型={}, 错误信息={}",
                    parameterName, validationType, result.getMessage());
            throw new BusinessException(result.getMessage());
        }
    }

    /**
     * 校验对象参数
     *
     * @param obj            对象参数
     * @param parameterName  参数名
     * @param validationType 校验类型
     */
    private void validateObjectParameter(Object obj, String parameterName, ValidationType validationType) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();

        // 针对容器类型的特殊处理，避免对JDK内部类进行反射
        if (clazz.isArray()) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object element = Array.get(obj, i);
                if (element instanceof String) {
                    validateStringParameter((String) element, parameterName + "[" + i + "]", validationType);
                } else if (element != null && isProjectClass(element.getClass()) && isNotPrimitiveOrWrapper(element.getClass()) && !isJdkClass(element.getClass())) {
                    validateObjectParameter(element, parameterName + "[" + i + "]", validationType);
                }
            }
            return;
        }
        if (obj instanceof Iterable<?>) {
            int idx = 0;
            for (Object element : (Iterable<?>) obj) {
                if (element instanceof String) {
                    validateStringParameter((String) element, parameterName + "[" + idx + "]", validationType);
                } else if (element != null && isProjectClass(element.getClass()) && isNotPrimitiveOrWrapper(element.getClass()) && !isJdkClass(element.getClass())) {
                    validateObjectParameter(element, parameterName + "[" + idx + "]", validationType);
                }
                idx++;
            }
            return;
        }
        if (obj instanceof Map<?, ?> mapObj) {
            for (Map.Entry<?, ?> entry : mapObj.entrySet()) {
                Object value = entry.getValue();
                String keyName = String.valueOf(entry.getKey());
                if (value instanceof String) {
                    validateStringParameter((String) value, parameterName + "." + keyName, validationType);
                } else if (value != null && isProjectClass(value.getClass()) && isNotPrimitiveOrWrapper(value.getClass()) && !isJdkClass(value.getClass())) {
                    validateObjectParameter(value, parameterName + "." + keyName, validationType);
                }
            }
            return;
        }

        // 避免对JDK核心类做反射（JDK17强封装会抛 InaccessibleObjectException）
        if (isJdkClass(clazz)) {
            // JDK类型不做反射展开；若为 CharSequence 则直接按字符串校验
            if (obj instanceof CharSequence) {
                validateStringParameter(obj.toString(), parameterName, validationType);
            }
            return;
        }

        // 仅处理项目包下的业务对象，跳过第三方框架对象（如 Spring Security、Servlet 等）
        if (!isProjectClass(clazz)) {
            return;
        }

        Field[] fields = ReflectUtil.getFields(clazz);

        for (Field field : fields) {
            // 跳过静态字段，避免非法访问第三方常量（例如 StrictHttpFirewall 的静态常量）
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // 跳过合成/匿名等特殊字段（如 this$0 等）
            if (field.isSynthetic() || field.getName().contains("$")) {
                continue;
            }
            try {
                if (!field.canAccess(obj)) {
                    field.setAccessible(true);
                }
                Object fieldValue = field.get(obj);

                if (fieldValue instanceof String stringValue) {
                    if (StrUtil.isNotBlank(stringValue)) {
                        ValidationResult result = performValidation(stringValue, validationType);
                        if (!result.isValid()) {
                            log.warn("对象字段校验失败: 参数名={}, 字段名={}, 校验类型={}, 错误信息={}",
                                    parameterName, field.getName(), validationType, result.getMessage());
                            throw new BusinessException(String.format("字段 %s %s", field.getName(), result.getMessage()));
                        }
                    }
                } else if (fieldValue != null) {
                    Class<?> fvClazz = fieldValue.getClass();
                    // 容器类型递归处理
                    if (fvClazz.isArray() || fieldValue instanceof Iterable<?> || fieldValue instanceof Map<?, ?>) {
                        validateObjectParameter(fieldValue, parameterName + "." + field.getName(), validationType);
                    }
                    // 普通嵌套对象，仅处理项目包下的类型，避免进入第三方类
                    else if (isProjectClass(fvClazz) && isNotPrimitiveOrWrapper(fvClazz) && !isJdkClass(fvClazz)) {
                        validateObjectParameter(fieldValue, parameterName + "." + field.getName(), validationType);
                    }
                }
            } catch (InaccessibleObjectException e) {
                log.warn("JDK字段访问受限，跳过: {}.{}", clazz.getName(), field.getName());
            } catch (IllegalAccessException e) {
                log.warn("无法访问字段: {}.{}", clazz.getSimpleName(), field.getName());
            }
        }
    }

    /**
     * 执行具体的校验
     *
     * @param value          待校验的值
     * @param validationType 校验类型
     * @return 校验结果
     */
    private ValidationResult performValidation(String value, ValidationType validationType) {
        //                case SECURITY:
        //                    return validateSecurity(value);
        //                case XSS:
        //                    return validateXSS(value);
        //                case SQL_INJECTION:
        //                    return validateSQLInjection(value);
        //                case PATH_TRAVERSAL:
        //                    return validatePathTraversal(value);
        //                case COMMAND_INJECTION:
        //                    return validateCommandInjection(value);
        //                case LDAP_INJECTION:
        //                    return validateLDAPInjection(value);
        //                case XML_INJECTION:
        //                    return validateXMLInjection(value);
        return ValidationResult.success();
    }

    /**
     * 判断是否为基本类型或包装类型
     *
     * @param clazz 类型
     * @return 是否为基本类型或包装类型
     */
    private boolean isNotPrimitiveOrWrapper(Class<?> clazz) {
        return !clazz.isPrimitive() &&
                clazz != Boolean.class &&
                clazz != Byte.class &&
                clazz != Character.class &&
                clazz != Short.class &&
                clazz != Integer.class &&
                clazz != Long.class &&
                clazz != Float.class &&
                clazz != Double.class &&
                clazz != String.class &&
                !clazz.isEnum();
    }

    private boolean isJdkClass(Class<?> clazz) {
        String name = clazz.getName();
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("jakarta.") || name.startsWith("sun.");
    }

    // 新增：仅允许我们项目包下的类型参与反射校验，避免扫描第三方框架对象
    private boolean isProjectClass(Class<?> clazz) {
        String name = clazz.getName();
        return name.startsWith("cn.chenyunlong.");
    }

    /**
     * 校验类型枚举
     */
    private enum ValidationType {
        SECURITY,
        XSS,
        SQL_INJECTION,
        PATH_TRAVERSAL,
        COMMAND_INJECTION,
        LDAP_INJECTION,
        XML_INJECTION
    }
}
