package cn.chenyunlong.qing.domain.zan.pipeline;

import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;

public interface EventFilter<T extends EventContext> {

    /**
     * 过滤事件
     *
     * @param event 事件
     */
    void doFilter(T event);

}
