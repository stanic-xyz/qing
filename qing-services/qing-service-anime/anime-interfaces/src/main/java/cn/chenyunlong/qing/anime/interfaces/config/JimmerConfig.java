package cn.chenyunlong.qing.anime.interfaces.config;

import org.babyfish.jimmer.jackson.ImmutableModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JimmerConfig {

    // Jimmer内部代码，非用户代码
    @ConditionalOnMissingBean(ImmutableModule.class)
    @Bean
    public ImmutableModule immutableModule() {
        return new ImmutableModule();
    }

}
