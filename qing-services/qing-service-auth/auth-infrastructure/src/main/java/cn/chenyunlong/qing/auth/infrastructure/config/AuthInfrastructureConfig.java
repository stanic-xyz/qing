package cn.chenyunlong.qing.auth.infrastructure.config;

import cn.chenyunlong.qing.infrastructure.email.IEmailServiceImpl;
import cn.chenyunlong.qing.infrastructure.email.api.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
@EntityScan(basePackages = "cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity")
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository")
public class AuthInfrastructureConfig {

    @Bean
    public IEmailService emailService() {
        return new IEmailServiceImpl();
    }
}
