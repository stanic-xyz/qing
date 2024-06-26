package cn.chenyunlong.qing.domain.auth.department;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import jakarta.persistence.Convert;
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

    private Integer sortNum;

    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    public void init() {
        setValidStatus(ValidStatus.VALID);
    }

    public void valid() {
        setValidStatus(ValidStatus.VALID);
    }

    public void invalid() {
        setValidStatus(ValidStatus.INVALID);
    }
}
