package cn.chenyunlong.qing.anime.interfaces.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.chenyunlong.common.mapper.DateMapper;

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
    public DateMapper dateMapper() {
        return new DateMapper();
    }
}
