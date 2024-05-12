package cn.chenyunlong.qing.domain.zan.pipeline.context;

import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import cn.chenyunlong.qing.domain.zan.pipeline.selecter.FilterSelector;

/**
 * 业务上下文。
 *
 * @author 陈云龙
 */
public interface EventContext {

    /**
     * 获取业务代码。
     * 要使用代码必须定义枚举值，不允许使用无效的业务代码
     *
     * @return 业务类型
     */
    BizEnum getBizEnum();

    /**
     * 过滤器选择器。
     *
     * <p>可以通过这个对象来过滤掉不需要的业务处理器例如在业务中，我们暂时不需要调用某个业务，就可以在配置里面暂时关掉该业务的调用，可以在 Nacos等配置中心或者配置文件中进行配置<p/>
     */
    FilterSelector getFilterSelector();

    /**
     * 是否继续执行过滤器。
     */
    boolean continueChain();

}
