package chenyunlong.zhangli.core.exception;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stan
 */
@ControllerAdvice
public class CustomExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String, Object> errorHandler(Exception ex) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", 400);
        //判断异常的类型,返回不一样的返回值
        if (ex instanceof MissingServletRequestParameterException) {
            map.put("msg", "缺少必需参数：" + ((MissingServletRequestParameterException) ex).getParameterName());
        } else if (ex instanceof AbstractException) {
            map.put("msg", ex.getMessage());
        } else {
            map.put("msg", "发生了其他错误：" + ex.getMessage());
        }
        return map;
    }
}