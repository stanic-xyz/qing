package cn.chenyunlong.qing.domain.zan.pipeline.selecter;

import cn.chenyunlong.qing.domain.zan.pipeline.EventFilter;
import cn.chenyunlong.qing.domain.zan.pipeline.context.EventContext;
import java.util.List;

public class LocalListBasedFilterSelector implements FilterSelector {

    private final List<String> filterNames;

    public LocalListBasedFilterSelector(List<String> filterNames) {
        this.filterNames = filterNames;
    }

    @Override
    public boolean matchFilter(EventFilter<? extends EventContext> eventFilter) {
        return filterNames.contains(eventFilter.getClass().getSimpleName());
    }
}
