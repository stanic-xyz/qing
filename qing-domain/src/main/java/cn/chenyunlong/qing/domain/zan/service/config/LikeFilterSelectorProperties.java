package cn.chenyunlong.qing.domain.zan.service.config;

import cn.chenyunlong.qing.domain.zan.pipeline.BizEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "like")
public class LikeFilterSelectorProperties {

    /**
     * 配置业务所需的过滤器链。
     */
    private Map<BizEnum, List<String>> configs;
}
