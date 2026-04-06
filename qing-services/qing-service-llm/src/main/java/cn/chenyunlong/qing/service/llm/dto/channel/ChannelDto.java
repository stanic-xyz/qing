package cn.chenyunlong.qing.service.llm.dto.channel;

import cn.chenyunlong.qing.service.llm.entity.Channel;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ChannelDto {

    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // 渠道编码 (如 ALIPAY, WECHAT)

    private String name; // 渠道名称
    private String icon; // 渠道图标

    private String status; // 审批状态: DRAFT, PENDING, EFFECTIVE, REJECTED
    private Boolean isEnabled = true; // 是否启用

    public static ChannelDto of(Channel channel) {
        if (channel == null) return null;
        ChannelDto channelDto = new ChannelDto();
        channelDto.setId(channel.getId());
        channelDto.setCode(channel.getCode());
        channelDto.setName(channel.getName());
        channelDto.setIcon(channel.getIcon());
        channelDto.setStatus(channel.getStatus());
        channelDto.setIsEnabled(channel.getIsEnabled());
        return channelDto;
    }

}
