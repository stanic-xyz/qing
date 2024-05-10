package cn.chenyunlong.qing.domain.zan.service.plugin;

import cn.chenyunlong.qing.domain.zan.LikeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TenXunYunSmsPluginProvider implements SmsPlugin {

    private static final Logger log = LoggerFactory.getLogger(TenXunYunSmsPluginProvider.class);

    @Override
    public boolean supports(LikeModel smsType) {
        return smsType.getSmsType() == SmsType.TX_YUN;
    }

    @Override
    public void sendSms(String phone, String content) {
        log.info("通过腾讯云渠道 给phone:[{}]发送短信:[{}]成功", phone, content);
    }
}
