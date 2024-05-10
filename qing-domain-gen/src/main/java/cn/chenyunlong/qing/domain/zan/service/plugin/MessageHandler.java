package cn.chenyunlong.qing.domain.zan.service.plugin;

import cn.chenyunlong.qing.domain.zan.LikeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageHandler implements SmsPlugin {

    @Override
    public boolean supports(LikeModel likeModel) {
        return likeModel.getSmsType() == SmsType.A_LI_YUN;
    }

    @Override
    public void sendSms(String phone, String content) {
        log.info("调用Plugin处理业务扩展！");
    }
}
