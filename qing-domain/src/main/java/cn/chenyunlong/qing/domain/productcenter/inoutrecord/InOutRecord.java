package cn.chenyunlong.qing.domain.productcenter.inoutrecord;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.converter.ValidStatusConverter;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.convert.DirectionTypeConverter;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.convert.InOutStoreTypeConverter;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.DirectionType;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.enums.InOutStoreType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "in_out_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class InOutRecord extends BaseJpaAggregate {

    @FieldDesc(name = "仓库ID")
    private Long storeId;

    @FieldDesc(name = "出入库类型")
    @Convert(converter = InOutStoreTypeConverter.class)
    private InOutStoreType inOutStoreType;

    @FieldDesc(name = "出入方向")
    @Convert(converter = DirectionTypeConverter.class)
    private DirectionType directionType;

    @FieldDesc(name = "批次号")
    private String batchNo;

    @FieldDesc(name = "数量")
    private Integer count;

    @Convert(converter = ValidStatusConverter.class)
    private ValidStatus validStatus;

    @FieldDesc(name = "操作人")
    private String operateUser;

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
