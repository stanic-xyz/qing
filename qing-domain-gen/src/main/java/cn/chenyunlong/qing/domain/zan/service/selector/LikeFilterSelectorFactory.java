package cn.chenyunlong.qing.domain.zan.service.selector;

import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import cn.chenyunlong.qing.domain.zan.pipeline.selecter.FilterSelector;

public interface LikeFilterSelectorFactory {

    FilterSelector getFilterSelector(BizEnum bizEnum);
}
