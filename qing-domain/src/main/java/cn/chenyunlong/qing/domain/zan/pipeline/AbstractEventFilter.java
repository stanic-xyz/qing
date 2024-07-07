package cn.chenyunlong.qing.domain.zan.pipeline;

import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;

/**
 * 默认处理器
 * 设计模式：模板方法
 *
 * @param <T> 参数泛型
 * @author 陈云龙
 */
public abstract class AbstractEventFilter<T extends EventContext> implements EventFilter<T> {

    /**
     * 执行过滤器事件
     *
     * @param context 上下文
     */
    @Override
    public void doFilter(T context) {
        if (context.getFilterSelector().matchFilter(this)) {
            handleEvent(context);
        }
    }

    protected abstract void handleEvent(T eventContext);

}
