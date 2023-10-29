package cn.chenyunlong.qing.domain.zan;

import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;
import cn.chenyunlong.qing.domain.zan.pipeline.selecter.DefaultFilterSelector;
import cn.chenyunlong.qing.domain.zan.pipeline.selecter.FilterSelector;
import cn.chenyunlong.qing.domain.zan.request.ZanCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 点赞上下文
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LikeContext implements EventContext {

    /**
     * 业务代码
     */
    private final BizEnum bizEnum;

    /**
     * 过滤器选择器
     */
    private final FilterSelector filterSelector;
    @Getter
    private final LikeModel likeModel;
    @Getter
    @Setter
    private ZanCreateRequest createRequest;

    /**
     * 新建一个上下文。
     *
     * @param bizEnum        业务代码
     * @param filterSelector 过滤器选择器
     * @return 业务上下文
     */
    public static LikeContext create(BizEnum bizEnum,
                                     FilterSelector filterSelector) {
        return new LikeContext(bizEnum, filterSelector, LikeModel.createDefault());
    }

    /**
     * 创建一个带有默认选择器的的上下文。
     *
     * @param bizEnum 业务代码
     * @return 业务上下文
     */
    public static LikeContext create(BizEnum bizEnum) {
        return new LikeContext(bizEnum, null, LikeModel.createDefault());
    }

    /**
     * 获取过滤器选择器。
     */
    @Override
    public FilterSelector getFilterSelector() {
        // 如果没有配置匹配器，返回一个匹配所有过滤器的匹配器。
        return filterSelector != null ? filterSelector : DefaultFilterSelector.createDefault();
    }

    @Override
    public boolean continueChain() {
        return true;
    }
}
