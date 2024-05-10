package cn.chenyunlong.qing.domain.auth.role;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "role")
@ToString
public class Role extends BaseJpaAggregate {

    @FieldDesc(name = "角色编码")
    private String role;

    @FieldDesc(name = "角色名称")
    private String name;

    @FieldDesc(name = "平台Id")
    private Long platformId;

    @FieldDesc(name = "备注")
    private String remark;

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
