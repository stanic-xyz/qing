package cn.chenyunlong.qing.domain.zan.pipeline;

import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;
import lombok.Getter;
import lombok.Setter;


/**
 * 默认的过滤器链。
 */
@Getter
public class DefaultFilterChain<T extends EventContext> implements EventFilterChain<T> {

    /**
     * 流水线的描述信息
     */
    private final String description;

    @Setter
    private EventFilterChain<T> next;
    private final EventFilter<T> eventFilter;

    /**
     * 初始化一个默认的过滤器链
     *
     * @param description 过滤器链描述信息
     * @param last        最后一个链接
     * @param eventFilter 过滤器
     */
    public DefaultFilterChain(String description,
                              EventFilterChain<T> last,
                              EventFilter<T> eventFilter) {
        this.next = last;
        this.eventFilter = eventFilter;
        this.description = description;
    }

    @Override
    public void fireNext(T context) {
        next.handle(context);
    }

    @Override
    public void handle(T context) {
        eventFilter.doFilter(context);
        if (next != null && context.continueChain()) {
            fireNext(context);
        }
    }
}
