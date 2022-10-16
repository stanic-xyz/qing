package cn.chenyunlong.zhangli.core.exception;

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
    @ExceptionHandler(value = AbstractException.class)
    public Map<String, Object> errorHandler(Exception ex) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", 400);
        //判断异常的类型,返回不一样的返回值
        map.put("msg", ex.getMessage());
        return map;
    }
}