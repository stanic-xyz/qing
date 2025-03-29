package cn.chenyunlong.qing.domain.zan.config;

import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.zan.LikeContext;
import cn.chenyunlong.qing.domain.zan.filters.EntityQueryFilter;
import cn.chenyunlong.qing.domain.zan.filters.UserPayFilter;
import cn.chenyunlong.qing.domain.zan.filters.UserQueryFilter;
import cn.chenyunlong.qing.domain.zan.pipeline.FilterChainPipeline;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LikePipelineConfig {

    private final UserRepository userRepository;

    /**
     * 创建点赞服务 Pipeline 。
     *
     * @return 点赞服务的过滤器链
     */
    @Bean
    public FilterChainPipeline<LikeContext> likeContextPipeline() {
        FilterChainPipeline<LikeContext> chainPipeline = new FilterChainPipeline<>();
        chainPipeline
            .addLast("用户支付", userPayFilter())
            .addFirst("查询实体信息", entityQueryFilter())
            .addFirst("查询用户信息", userQueryFilter());
        return chainPipeline;
    }

    @Bean
    public UserPayFilter userPayFilter() {
        return new UserPayFilter();
    }

    @Bean
    public UserQueryFilter userQueryFilter() {
        return new UserQueryFilter(userRepository);
    }

    @Bean
    public EntityQueryFilter entityQueryFilter() {
        return new EntityQueryFilter();
    }
}
