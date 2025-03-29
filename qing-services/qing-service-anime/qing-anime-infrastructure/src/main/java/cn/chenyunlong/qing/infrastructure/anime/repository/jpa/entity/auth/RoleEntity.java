package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private String role;

    @FieldDesc(name = "角色名称")
    private String name;

    @FieldDesc(name = "平台Id", description = "默认为空")
    private Long platformId;

    @FieldDesc(name = "备注")
    private String remark;

}
