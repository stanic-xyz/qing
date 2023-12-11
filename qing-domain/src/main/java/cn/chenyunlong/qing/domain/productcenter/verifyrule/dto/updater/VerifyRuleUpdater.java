package cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.verifyrule.VerifyRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class VerifyRuleUpdater {
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

    public void updateVerifyRule(VerifyRule param) {
        Optional.ofNullable(getRuleName()).ifPresent(param::setRuleName);
        Optional.ofNullable(getRegexStr()).ifPresent(param::setRegexStr);
        Optional.ofNullable(getMessage()).ifPresent(param::setMessage);
        Optional.ofNullable(getVerifySchema()).ifPresent(param::setVerifySchema);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
