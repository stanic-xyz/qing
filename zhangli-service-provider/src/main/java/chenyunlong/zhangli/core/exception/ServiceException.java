package chenyunlong.zhangli.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhoutaoo
 * @date 2018/6/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends BaseException {
    public ServiceException(String s) {
        super();
    }

}
