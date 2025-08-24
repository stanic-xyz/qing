package cn.chenyunlong.qing.anime.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.chenyunlong.qing.anime.application.port.AnimeQueryPort;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.anime.infrastructure.adapter.AnimeQueryAdapter;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeInfrastructureMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.AnimeRepositoryJpaImpl;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 动漫基础设施层配置
 * 配置仓储实现、适配器和映射器
 *
 * @author chenyunlong
 * @since 2024-12-30
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository")
@EntityScan(basePackages = "cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity")
public class AnimeInfrastructureConfig {

    /**
     * 动漫仓储实现
     *
     * @param animeJpaRepository JPA仓储
     * @param mapper             映射器
     * @return 动漫仓储实现
     */
    @Bean
    public AnimeRepository animeRepository(
            AnimeJpaRepository animeJpaRepository,
            AnimeInfrastructureMapper mapper) {
        log.info("配置动漫仓储实现");
        return new AnimeRepositoryJpaImpl(animeJpaRepository, mapper);
    }

    /**
     * 动漫查询端口适配器
     *
     * @param animeJpaRepository JPA仓储
     * @param mapper             映射器
     * @return 动漫查询端口适配器
     */
    @Bean
    public AnimeQueryPort animeQueryPort(
            AnimeJpaRepository animeJpaRepository,
            AnimeInfrastructureMapper mapper) {
        log.info("配置动漫查询端口适配器");
        return new AnimeQueryAdapter(animeJpaRepository, mapper);
    }
}
