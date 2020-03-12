package chenyunlong.zhangli.config;

import com.riversoft.weixin.mp.base.AppSetting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MpConfig {

    @Value("${mp.wx.appId}")
    private String appId;
    @Value("${mp.wx.secret}")
    private String secret;

    @Bean
    public AppSetting getAppSetting() {
        return new AppSetting("wx09bf7e704a150a9c", "9fdb9fa846356d32247a9aa68d758791");
    }
}
