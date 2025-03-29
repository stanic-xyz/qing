package cn.chenyunlong.qing.infrastructure.anime.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "cn.chenyunlong.qing.infrastructure.anime.converter",
    "cn.chenyunlong.qing.infrastructure.anime.repository"
})
@EntityScan(basePackages = {
    "cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity"
})
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository")
public class RepositoryAutoConfiguration implements InitializingBean {


    @Override
    public void afterPropertiesSet() {
        log.info("基础设施层-Repository模块自动配置加载成功");
    }
}
