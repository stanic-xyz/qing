package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.auth;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "sys_platform")
public class PlatformEntity extends BaseJpaEntity {

    @Column(unique = true)
    @FieldDesc(name = "编码")
    private String code;

    @Column(unique = true)
    @FieldDesc(name = "平台名称")
    private String name;
}
