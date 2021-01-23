package chenyunlong.zhangli.aspect;

import chenyunlong.zhangli.common.annotation.JsonFieldFilter;
import chenyunlong.zhangli.model.vo.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;


/**
 * 通过实现ResponseBodyAdvice接口来完成对响应结果的过滤
 * author
 */
@ControllerAdvice
public class JsonFilterAdvice implements ResponseBodyAdvice<ApiResult> {

    private final Logger log = LoggerFactory.getLogger(JsonFilterAdvice.class);

    /**
     * 判断是否进行过滤
     *
     * @param methodParameter 方法参数
     * @param converterClass  转换函数
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterClass) {
        List<Annotation> annotations = Arrays.asList(methodParameter.getMethodAnnotations());
        return annotations.stream().anyMatch(annotation -> annotation.annotationType().equals(JsonFieldFilter.class));
    }

    /**
     * 对最后的响应结果进行修改
     *
     * @param apiResult
     * @param methodParameter
     * @param mediaType
     * @param convertClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public ApiResult beforeBodyWrite(ApiResult apiResult, MethodParameter methodParameter, MediaType mediaType,
                                     Class<? extends HttpMessageConverter<?>> convertClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        //获取方法的相关注解
        JsonFieldFilter annotation = methodParameter.getMethodAnnotation(JsonFieldFilter.class);

        //获取需要进行过滤的字段或者其他东西！
        assert annotation != null;
        List<String> possibleFilters = Arrays.asList(annotation.filters());

//        直接修改返回值中的结果
//        baseResponse.setMsg("这是被修改过的");
//        修改响应头
//        serverHttpResponse.getHeaders().setContentType(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE));

//        通过Lambda表达式，实现Runnable接口来进行创建线程，
        log.debug("这里还是在主线程之中运行的呢！");
        new Thread(() -> {
            log.debug("测试其他任务");
        }).start();

        // 这里暂时不需要进行处理
        return apiResult;
    }
}
