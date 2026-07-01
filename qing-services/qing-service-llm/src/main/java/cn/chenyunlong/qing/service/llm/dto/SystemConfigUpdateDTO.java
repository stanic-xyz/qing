package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;

/**
 * 系统配置更新请求。
 */
@Data
public class SystemConfigUpdateDTO {
    private String value;
    private String description;
}
