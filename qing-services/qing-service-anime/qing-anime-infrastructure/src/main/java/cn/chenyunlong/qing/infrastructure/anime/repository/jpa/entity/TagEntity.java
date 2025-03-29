package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "anime_tag")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TagEntity extends BaseJpaEntity {

    @NotBlank(message = "名称不能为空")
    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "介绍")
    private String instruction;
}
