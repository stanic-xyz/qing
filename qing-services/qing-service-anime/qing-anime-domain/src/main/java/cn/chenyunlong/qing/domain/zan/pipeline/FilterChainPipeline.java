package cn.chenyunlong.qing.domain.zan.pipeline;


import cn.chenyunlong.qing.domain.zan.LikeContext;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

/**
 * Filter组成的流水线，每一条流水线代表了一种业务。
 */
@Getter
public class FilterChainPipeline<T extends LikeContext> {

    /**
     * 获取所有的过滤器链。
     */
    private final List<EventFilter<T>> filters;
    /**
     * 下一个过滤器。
     */
    private EventFilterChain<T> next;
    /**
     * 最后一个过滤器。
     */
    @Setter
    private EventFilterChain<T> last;

    public FilterChainPipeline() {
        filters = new LinkedList<>();
    }

    /**
     * 添加过滤器到第一个。
     *
     * @param description 描述信息
     * @param filter      过滤器
     * @return 当前流水线, 始终返回过滤器流水线的第一个链
     */
    public FilterChainPipeline<T> addFirst(String description, EventFilter<T> filter) {
        filters.add(0, filter);
        DefaultFilterChain<T> filterChain =
            new DefaultFilterChain<>(description, this.next, filter);
        this.next = filterChain;
        if (this.last == null) {
            this.last = filterChain;
        }
        return this;
    }

    /**
     * 添加过滤器到最后一个。
     *
     * @param description 描述信息
     * @param filter      过滤器
     * @return 当前流水线, 始终返回过滤器流水线的第一个链
     */
    public FilterChainPipeline<T> addLast(String description, EventFilter<T> filter) {
        filters.add(filters.size(), filter);
        DefaultFilterChain<T> filterChain = new DefaultFilterChain<>(description, null, filter);
        if (this.last != null) {
            this.last.setNext(filterChain);
        }
        if (this.next == null) {
            this.next = filterChain;
        }
        this.last = filterChain;
        return this;
    }

}
