package cn.chenyunlong.qing.domain.zan.filters;

import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.pipeline.AbstractEventFilter;
import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserPayFilter extends AbstractEventFilter<LikeContext> {
    @Override
    protected void handleEvent(LikeContext eventContext) {
        BizEnum bizEnum = eventContext.getBizEnum();
        log.info("用户支付:业务代码{}，用户Id：{}", bizEnum,
            eventContext.getCreateRequest().getUserId());
    }
}
