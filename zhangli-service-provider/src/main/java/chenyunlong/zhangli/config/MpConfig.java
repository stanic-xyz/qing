package chenyunlong.zhangli.config;

import chenyunlong.zhangli.config.properties.WxProperties;
import com.riversoft.weixin.mp.base.AppSetting;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author stan
 */
@Configuration
@EnableConfigurationProperties(WxProperties.class)
public class MpConfig implements InitializingBean {

    @Resource
    private WxProperties wxProperties;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    public AppSetting getAppSetting() {
        return new AppSetting(wxProperties.getAppId(), wxProperties.getSecret());
    }
}
