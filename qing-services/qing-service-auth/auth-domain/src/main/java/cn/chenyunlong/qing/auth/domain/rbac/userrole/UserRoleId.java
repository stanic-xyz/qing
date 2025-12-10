package cn.chenyunlong.qing.auth.domain.rbac.userrole;

import cn.chenyunlong.qing.domain.common.Identifiable;
import cn.hutool.core.util.IdUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserRoleId implements Identifiable<Long> {

    private Long id;

    public static UserRoleId nextId() {
        return new UserRoleId(IdUtil.getSnowflakeNextId());
    }

    @Override
    public Long id() {
        return id;
    }


}
