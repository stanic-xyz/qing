package cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class VerifyRuleResponse extends AbstractJpaResponse {

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

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
