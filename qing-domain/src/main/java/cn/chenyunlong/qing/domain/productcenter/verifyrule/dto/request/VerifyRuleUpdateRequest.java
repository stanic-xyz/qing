package cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class VerifyRuleUpdateRequest implements Request {
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

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
