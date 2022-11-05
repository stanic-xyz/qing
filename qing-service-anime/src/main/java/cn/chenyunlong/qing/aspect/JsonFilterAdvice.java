/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.aspect;

import cn.chenyunlong.qing.annotation.JsonFieldFilter;
import cn.chenyunlong.qing.core.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;


/**
 * 通过实现ResponseBodyAdvice接口来完成对响应结果的过滤
 *
 * @author Stan
 */
@Slf4j
@SuppressWarnings("rawtypes")
@ControllerAdvice
public class JsonFilterAdvice implements ResponseBodyAdvice<ApiResult> {

    /**
     * 判断是否进行过滤
     *
     * @param methodParameter 方法参数
     * @param converterClass  转换函数
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> converterClass) {
        List<Annotation> annotations = Arrays.asList(methodParameter.getMethodAnnotations());
        return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(JsonFieldFilter.class));
    }

    /**
     * 写出结果前
     *
     * @param apiResult          返回结果
     * @param methodParameter    方法仓鼠信息
     * @param mediaType          返回的media类型
     * @param convertClass       转换的但是
     * @param serverHttpRequest  httpRequest
     * @param serverHttpResponse httpResponse
     * @return 请求的结果
     */
    @Override
    public ApiResult beforeBodyWrite(ApiResult apiResult,
                                     MethodParameter methodParameter,
                                     @NonNull MediaType mediaType,
                                     @NonNull Class<? extends HttpMessageConverter<?>> convertClass,
                                     @NonNull ServerHttpRequest serverHttpRequest,
                                     @NonNull ServerHttpResponse serverHttpResponse) {

        //获取方法的相关注解
        JsonFieldFilter annotation = methodParameter.getMethodAnnotation(JsonFieldFilter.class);
        //获取需要进行过滤的字段或者其他东西！
        if (annotation != null) {
            //需要过滤的字段列表
            List<String> possibleFilters = Arrays.asList(annotation.filters());
            if (log.isDebugEnabled()) {
                log.debug("需要过滤的字段列表{}", possibleFilters);
            }
            apiResult.setMsg(apiResult.getMsg() + "：Modified");
        }
        serverHttpResponse.getHeaders().setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));
        // 这里暂时不需要进行处理
        return apiResult;
    }
}
