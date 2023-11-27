package cn.chenyunlong.qing.domain.zan;

import cn.chenyunlong.qing.domain.auth.user.User;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.zan.service.plugin.SmsType;
import lombok.Data;

@Data
public class LikeModel {

    private SmsType smsType = SmsType.A_LI_YUN;
    private User user;
    private Entity entity;

    /**
     * 创建一个默认的上下文。
     */
    public static LikeModel createDefault() {
        return new LikeModel();
    }
}
