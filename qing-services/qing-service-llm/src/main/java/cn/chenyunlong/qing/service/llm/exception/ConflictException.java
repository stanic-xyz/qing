package cn.chenyunlong.qing.service.llm.exception;

import cn.chenyunlong.common.exception.AbstractException;
import org.springframework.http.HttpStatus;

/**
 * 表示资源当前状态或唯一性约束与本次操作发生冲突。
 */
public class ConflictException extends AbstractException {

    /**
     * 使用冲突提示信息创建异常。
     *
     * @param message 冲突提示信息
     */
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
