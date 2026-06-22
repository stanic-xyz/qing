package cn.chenyunlong.qing.service.llm.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 交易写操作成功后的变更事件。
 */
@Getter
public class TransactionChangeEvent extends ApplicationEvent {

    private final Long transactionId;
    private final Action action;

    public TransactionChangeEvent(Object source, Long transactionId, Action action) {
        super(source);
        this.transactionId = transactionId;
        this.action = action;
    }

    public enum Action {
        CREATED,
        UPDATED
    }
}
