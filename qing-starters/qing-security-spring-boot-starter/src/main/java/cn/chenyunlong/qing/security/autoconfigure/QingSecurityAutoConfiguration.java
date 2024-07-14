package cn.chenyunlong.qing.security.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan(basePackages = "cn.chenyunlong.qing.security.**")
public class QingSecurityAutoConfiguration implements InitializingBean {

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     */
    @Override
    public void afterPropertiesSet() {
        // 检查配置信息是否正确
        log.info("qing security auto configuration success");
    }
}
