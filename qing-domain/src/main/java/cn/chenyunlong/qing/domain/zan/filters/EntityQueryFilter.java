package cn.chenyunlong.qing.domain.zan.filters;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.repository.EntityRepository;
import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.pipeline.AbstractEventFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EntityQueryFilter extends AbstractEventFilter<LikeContext> {

    private final EntityRepository entityService;

    @Override
    protected void handleEvent(LikeContext eventContext) {
        log.info("获取实体信息！");
        ZanCreateRequest createRequest = eventContext.getCreateRequest();
        Long entityId = createRequest.getEntityId();
        Entity entity = entityService.findById(entityId)
            .orElseThrow(() -> new NotFoundException("未查询到实体信息：" + entityId));
        // 将处理信息添加到上下文中
        eventContext.getLikeModel().setEntity(entity);
    }
}
