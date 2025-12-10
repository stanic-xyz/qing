package cn.chenyunlong.qing.auth.domain.user.specification;

import cn.chenyunlong.qing.auth.domain.user.valueObject.RawPassword;

public class PasswordPolicySpec {
    // 业务规则在这里
    public boolean isSatisfiedBy(RawPassword rawPassword) {
        return rawPassword.isOk();
    }

}
