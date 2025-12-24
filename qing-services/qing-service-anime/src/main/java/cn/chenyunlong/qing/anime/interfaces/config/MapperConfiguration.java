package cn.chenyunlong.qing.anime.interfaces.config;

import cn.chenyunlong.qing.anime.infrastructure.converter.base.DateMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Mapper配置类
 * 用于配置MapStruct所需的Mapper Bean
 *
 * @author chenyunlong
 * @since 2025-08-24
 */
@Configuration
public class MapperConfiguration {

    /**
     * 配置DateMapper Bean
     * 用于MapStruct中的日期转换
     *
     * @return DateMapper实例
     */
    @Bean
    @ConditionalOnMissingBean
    public DateMapper dateMapper() {
        return new DateMapper();
    }
}
