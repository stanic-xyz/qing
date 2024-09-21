package cn.chenyunlong.qing.domain.auth.department;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "department")
public class Department extends BaseJpaAggregate {

    @FieldDesc(name = "部门名称")
    private String name;

    @FieldDesc(name = "上级单位")
    private Long pid;

    @FieldDesc(name = "排序号")
    private Integer sortNum;

}
