package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@Entity
@Table(name = "sys_role")
@ToString
public class RoleEntity extends BaseJpaEntity {

    @Unique
    @FieldDesc(name = "角色编码")
    private String code;

    @FieldDesc(name = "角色名称")
    private String name;

    @FieldDesc(name = "备注")
    private String remark;
}
