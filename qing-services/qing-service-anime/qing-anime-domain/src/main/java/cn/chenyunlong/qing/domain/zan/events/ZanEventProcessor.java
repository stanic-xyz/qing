package cn.chenyunlong.qing.domain.zan.events;

import cn.chenyunlong.qing.domain.zan.repository.ZanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ZanEventProcessor {

    private final ZanRepository zanRepository;


    /**
     * 保存充值流水
     *
     * @param event 点赞事件
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMoneyChargeEvent(ZanEvent.ZanCreateEvent event) {
    }
}
