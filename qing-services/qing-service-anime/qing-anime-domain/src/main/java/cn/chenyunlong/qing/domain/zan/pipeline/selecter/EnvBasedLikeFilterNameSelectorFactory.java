package cn.chenyunlong.qing.domain.zan.pipeline.selecter;

import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import cn.chenyunlong.qing.domain.zan.service.config.LikeFilterSelectorProperties;
import cn.chenyunlong.qing.domain.zan.service.selector.LikeFilterSelectorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(LikeFilterSelectorProperties.class)
public class EnvBasedLikeFilterNameSelectorFactory implements LikeFilterSelectorFactory {

    private final LikeFilterSelectorProperties likeFilterSelectorProperties;

    /**
     * 根据请求获取过滤器选择器。
     *
     * @param bizEnum 业务代码
     * @return 过滤器选择器
     */
    @Override
    public FilterSelector getFilterSelector(BizEnum bizEnum) {
        List<String> filterNames =
            likeFilterSelectorProperties.getConfigs()
                .getOrDefault(bizEnum, Collections.emptyList());
        return new LocalListBasedFilterSelector(filterNames);
    }
}
