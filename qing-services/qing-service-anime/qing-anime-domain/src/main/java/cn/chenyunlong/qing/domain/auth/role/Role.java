package cn.chenyunlong.qing.domain.auth.role;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
public class Role extends BaseAggregate {

    @Unique
    @FieldDesc(name = "角色编码")
    private String role;

    @FieldDesc(name = "角色名称")
    private String name;

    @FieldDesc(name = "平台Id", description = "默认为空")
    private Long platformId;

    @FieldDesc(name = "备注")
    private String remark;

}
