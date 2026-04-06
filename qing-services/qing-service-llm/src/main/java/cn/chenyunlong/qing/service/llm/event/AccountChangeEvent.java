package cn.chenyunlong.qing.service.llm.event;

import cn.chenyunlong.qing.service.llm.entity.Account;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AccountChangeEvent extends ApplicationEvent {

    @Getter
    private final Long accountId;

    // 记录修改前后的状态
    public AccountChangeEvent(Object source, Account account) {
        super(source);
        this.accountId = account.getId();
    }
}
