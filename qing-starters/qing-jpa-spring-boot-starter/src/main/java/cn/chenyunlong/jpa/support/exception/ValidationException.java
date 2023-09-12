package cn.chenyunlong.jpa.support.exception;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.ValidateResult;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationException extends BusinessException {

    private List<ValidateResult> result;

    public ValidationException(List<ValidateResult> list) {
        super(CodeEnum.ParamSetIllegal, list);
        this.result = list;
    }

}
