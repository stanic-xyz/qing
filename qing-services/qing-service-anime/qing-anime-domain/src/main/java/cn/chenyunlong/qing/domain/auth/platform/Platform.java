package cn.chenyunlong.qing.domain.auth.platform;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Platform extends BaseAggregate {

    @Column(unique = true)
    @FieldDesc(name = "编码")
    private String code;

    @Column(unique = true)
    @FieldDesc(name = "平台名称")
    private String name;
}
