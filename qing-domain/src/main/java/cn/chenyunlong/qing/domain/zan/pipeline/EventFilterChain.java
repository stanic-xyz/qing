package cn.chenyunlong.qing.domain.zan.pipeline;

import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;

public interface EventFilterChain<T extends EventContext> {

    /**
     * 执行下一个过滤器。
     *
     * @param context 任务上下文
     */
    void fireNext(T context);

    /**
     * 处理链。
     *
     * @param context 上下文
     */
    void handle(T context);

    /**
     * 设置下一个过滤器链
     */
    void setNext(EventFilterChain<T> next);
}
