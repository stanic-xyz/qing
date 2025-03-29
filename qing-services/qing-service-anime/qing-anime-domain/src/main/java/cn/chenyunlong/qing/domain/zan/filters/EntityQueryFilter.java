package cn.chenyunlong.qing.domain.zan.filters;

import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.pipeline.AbstractEventFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EntityQueryFilter extends AbstractEventFilter<LikeContext> {


    @Override
    protected void handleEvent(LikeContext eventContext) {
        log.info("获取实体信息！");
        ZanCreateRequest createRequest = eventContext.getCreateRequest();
        Long entityId = createRequest.getEntityId();
    }
}
