package stan.zhangli.natcross.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import stan.zhangli.natcross.common.model.ResultUtil;
import stan.zhangli.natcross.common.model.response.ApiResult;

import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    /**
//     * 用来处理bean validation异常
//     *
//     * @param ex 试题校验错误异常
//     * @return
//     */
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseBody
//    public ApiResult resolveConstraintViolationException(ConstraintViolationException ex) {
//        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
//        if (!CollectionUtils.isEmpty(constraintViolations)) {
//            StringBuilder msgBuilder = new StringBuilder();
//            for (ConstraintViolation constraintViolation : constraintViolations)
//                msgBuilder.append(constraintViolation.getMessage()).append(",");
//            String errorMessage = msgBuilder.toString();
//            if (errorMessage.length() > 1) {
//                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
//            }
//        }
//        return ResultUtil.fail("参数校验错误");
//    }

    /**
     * 处理法法错误
     *
     * @param ex 参数错误异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResult resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
        }
        return ResultUtil.fail("参数校验错误");
    }
}
