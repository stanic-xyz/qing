package cn.chenyunlong.qing.infrastructure.repository.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "cn.chenyunlong.qing.infrastructure.repository")
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.infrastructure.repository.jpa")
public class RepositoryAutoConfiguration implements InitializingBean {


    @Override
    public void afterPropertiesSet() {
        log.info("基础设施层-Repository模块自动配置加载成功");
    }
}
