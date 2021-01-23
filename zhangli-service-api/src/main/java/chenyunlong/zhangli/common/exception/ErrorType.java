package chenyunlong.zhangli.common.exception;

/**
 * @author Stan
 */
public interface ErrorType {
    /**
     * 返回code
     *
     * @return 错误码
     */
    String getCode();

    /**
     * 错误消息
     *
     * @return 提示消息
     */
    String getMsg();
}
