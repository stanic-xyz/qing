package cn.chenyunlong.qing.domain.base;

import cn.chenyunlong.common.validator.CreateGroup;

public interface CustomValidator<T> {

    void doValidate(T data, Class<CreateGroup> createGroupClass);
}
