package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "anime_category")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class CategoryEntity  extends BaseJpaEntity {

    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "父级id")
    private Long pid;

    @FieldDesc(description = "排序号")
    private Integer orderNo;
}
