package cn.chenyunlong.qing.domain.anime.recommend;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
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
@Table(name = "anime_recommend")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Recommend extends BaseJpaAggregate {

    @NotBlank(message = "名称不能为空")
    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "介绍")
    private String instruction;

    @FieldDesc(description = "推荐的动漫id")
    private Long animeId;

}
