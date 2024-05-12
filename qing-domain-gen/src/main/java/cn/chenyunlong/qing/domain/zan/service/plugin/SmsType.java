package cn.chenyunlong.qing.domain.zan.service.plugin;

import lombok.Getter;

@Getter
public enum SmsType {
    /**
     * 阿里云渠道发送短信
     */
    A_LI_YUN("aliyun", "阿里云渠道发送短信"),
    /**
     * 腾讯云渠道发送短信
     */
    TX_YUN("tengxunyun", "腾讯云渠道发送短信");

    private final String smsChannel;
    private final String desc;

    SmsType(String smsChannel, String desc) {
        this.smsChannel = smsChannel;
        this.desc = desc;
    }

}
