package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 陈云龙
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "anime_district")
public class DistrictEntity extends BaseJpaEntity {

    @FieldDesc(name = "地区")
    private String code;

    @FieldDesc(name = "名称")
    private String name;

}
