package cn.chenyunlong.jpa.support.exception;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.ValidateResult;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends BusinessException {

    private final List<ValidateResult> result;

    public ValidationException(List<ValidateResult> list) {
        super(CodeEnum.ParamSetIllegal, list);
        this.result = list;
    }

}
