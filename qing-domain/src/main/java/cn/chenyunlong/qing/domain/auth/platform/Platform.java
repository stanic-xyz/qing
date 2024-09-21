package cn.chenyunlong.qing.domain.auth.platform;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "sys_platform")
@Setter
@Getter
@Entity
public class Platform extends BaseJpaAggregate {

    @Column(unique = true)
    @FieldDesc(name = "编码")
    private String code;

    @Column(unique = true)
    @FieldDesc(name = "平台名称")
    private String name;
}
