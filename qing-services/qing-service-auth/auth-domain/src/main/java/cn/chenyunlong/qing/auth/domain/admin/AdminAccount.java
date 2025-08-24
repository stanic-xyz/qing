package cn.chenyunlong.qing.auth.domain.admin;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.auth.domain.admin.dto.creator.AdminAccountCreator;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAccount extends BaseAggregate<AggregateId> {

    @FieldDesc(name = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @FieldDesc(name = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @FieldDesc(name = "用户名")
    private String username;

    @FieldDesc(name = "用户ID")
    private String uid;

    @FieldDesc(name = "真实姓名")
    private String realName;

    @FieldDesc(name = "部门ID")
    private Long departmentId;

    @FieldDesc(name = "额外信息")
    private String extInfo;

    public static AdminAccount create(AdminAccountCreator creator) {
        return null;
    }
}
