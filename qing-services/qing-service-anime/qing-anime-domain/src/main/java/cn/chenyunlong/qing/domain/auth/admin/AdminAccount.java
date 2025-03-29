package cn.chenyunlong.qing.domain.auth.admin;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.hutool.core.util.IdUtil;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
public class AdminAccount extends BaseAggregate {

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
    @Column(columnDefinition = "text")
    private String extInfo;

    /**
     * 初始化方法，在创建聚合根时调用
     */
    public void init() {
        setValidStatus(ValidStatus.VALID);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        setPassword(encoder.encode(getPassword()));
        setUid(IdUtil.randomUUID().replaceAll("-", ""));
    }
}
