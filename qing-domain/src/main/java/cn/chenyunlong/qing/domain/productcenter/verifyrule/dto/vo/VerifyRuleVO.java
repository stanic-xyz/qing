package cn.chenyunlong.qing.domain.productcenter.verifyrule.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.verifyrule.VerifyRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
        callSuper = true
)
@NoArgsConstructor(
        access = AccessLevel.PROTECTED
)
public class VerifyRuleVO extends AbstractBaseJpaVo {
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

    public VerifyRuleVO(VerifyRule source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setRuleName(source.getRuleName());
        this.setRegexStr(source.getRegexStr());
        this.setMessage(source.getMessage());
        this.setVerifySchema(source.getVerifySchema());
        this.setValidStatus(source.getValidStatus());
    }
}
