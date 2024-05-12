package cn.chenyunlong.qing.domain.zan.pipeline.selecter;

import cn.chenyunlong.qing.domain.zan.pipeline.EventFilter;
import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;

/**
 * 默认的匹配器，默认匹配所有过滤器。
 *
 * @author 陈云龙 on 2023/10/23
 */
public class DefaultFilterSelector implements FilterSelector {

    /**
     * 禁用构造器
     */
    private DefaultFilterSelector() {
    }

    /**
     * 创建一个默认的过滤器匹配器。
     */
    public static FilterSelector createDefault() {
        return new DefaultFilterSelector();
    }

    @Override
    public boolean matchFilter(EventFilter<? extends EventContext> eventFilter) {
        return true;
    }
}
