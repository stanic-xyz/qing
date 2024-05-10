package cn.chenyunlong.qing.domain.zan.pipeline.selecter;

import cn.chenyunlong.qing.domain.zan.pipeline.EventFilter;
import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;

public interface FilterSelector {


    /**
     * 根据过滤器的名称匹配过滤器。
     *
     * @param eventFilter 过滤器名称
     */
    boolean matchFilter(EventFilter<? extends EventContext> eventFilter);
}
