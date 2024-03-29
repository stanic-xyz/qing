package cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class VerifyRuleCreator {

    @Schema(
        title = "ruleName",
        description = "规则名称"
    )
    private String ruleName;

    @Schema(
        title = "regexStr",
        description = "校验表达式"
    )
    private String regexStr;

    @Schema(
        title = "message",
        description = "错误提示信息"
    )
    private String message;

    @Schema(
        title = "verifySchema",
        description = "jc"
    )
    private String verifySchema;
}
