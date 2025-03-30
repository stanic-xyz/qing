package cn.chenyunlong.qing.anime.infrastructure.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RepositoryAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        //        log.info("基础设施层-Repository模块自动配置加载成功");
    }
}
