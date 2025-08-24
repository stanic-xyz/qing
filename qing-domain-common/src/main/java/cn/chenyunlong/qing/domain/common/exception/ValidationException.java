package cn.chenyunlong.qing.domain.common.exception;

import java.util.List;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.ValidateResult;
import lombok.Getter;

@Getter
public class ValidationException extends BusinessException {

    private final List<ValidateResult> result;

    public ValidationException(List<ValidateResult> list) {
        super(CodeEnum.ParamSetIllegal, list);
        this.result = list;
    }

}
