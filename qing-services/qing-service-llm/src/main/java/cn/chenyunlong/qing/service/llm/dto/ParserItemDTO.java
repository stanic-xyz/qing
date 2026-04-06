package cn.chenyunlong.qing.service.llm.dto;

import cn.chenyunlong.qing.service.llm.dto.channel.ChannelDto;
import lombok.Data;

@Data
public class ParserItemDTO {
    private String id;          // 解析器ID (如: builtin:ALIPAY, custom:12)
    private String name;        // 显示名称
    private ChannelDto channel;     // 适用渠道
    private String fileType;    // 适用文件类型
    private Boolean isBuiltIn;  // 是否内置
}
