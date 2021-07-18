package chenyunlong.zhangli.common.config;

import com.riversoft.weixin.mp.base.AppSetting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author stan
 */
@Configuration
public class MpConfig {

    @Value("${mp.wx.appId}")
    private String appId;
    @Value("${mp.wx.secret}")
    private String secret;

    @Bean
    public AppSetting getAppSetting() {
        return new AppSetting(appId, secret);
    }
}
