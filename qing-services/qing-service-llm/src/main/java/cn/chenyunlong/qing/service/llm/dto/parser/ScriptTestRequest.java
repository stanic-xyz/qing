package cn.chenyunlong.qing.service.llm.dto.parser;

import lombok.Data;

import java.util.Map;

@Data
public class ScriptTestRequest {

    private String language = "groovy";

    private String script;

    private Map<String, Object> context;

    /**
     * true: 允许脚本返回 Map（用于后置脚本批量更新字段）
     * false: 禁止返回 Map（用于字段映射脚本，要求返回标量）
     */
    private Boolean allowMap = false;

    /**
     * true: 仅接受 Number 返回值（用于金额等场景）
     */
    private Boolean expectNumber = true;
}

