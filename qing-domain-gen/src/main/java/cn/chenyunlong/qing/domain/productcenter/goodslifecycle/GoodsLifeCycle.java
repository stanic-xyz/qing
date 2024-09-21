package cn.chenyunlong.qing.domain.productcenter.goodslifecycle;

import cn.chenyunlong.codegen.annotation.GenController;
import cn.chenyunlong.codegen.annotation.GenCreateRequest;
import cn.chenyunlong.codegen.annotation.GenCreator;
import cn.chenyunlong.codegen.annotation.GenFeign;
import cn.chenyunlong.codegen.annotation.GenMapper;
import cn.chenyunlong.codegen.annotation.GenQuery;
import cn.chenyunlong.codegen.annotation.GenQueryRequest;
import cn.chenyunlong.codegen.annotation.GenRepository;
import cn.chenyunlong.codegen.annotation.GenResponse;
import cn.chenyunlong.codegen.annotation.GenService;
import cn.chenyunlong.codegen.annotation.GenServiceImpl;
import cn.chenyunlong.codegen.annotation.GenUpdateRequest;
import cn.chenyunlong.codegen.annotation.GenUpdater;
import cn.chenyunlong.codegen.annotation.GenVo;
import cn.chenyunlong.codegen.annotation.IgnoreCreator;
import cn.chenyunlong.codegen.annotation.IgnoreUpdater;
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
@GenVo
@GenCreator
@GenUpdater
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenRepository
@GenService
@GenServiceImpl
@GenFeign(serverName = "stanic")
@GenController
@GenMapper
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "goods_life_cycle")
@Data
public class GoodsLifeCycle extends BaseJpaAggregate {

    @FieldDesc(name = "出入库方向")
    @Convert(converter = DirectionTypeConverter.class)
    private DirectionType directionType;

    @FieldDesc(name = "唯一编码")
    private String uniqueCode;

    @FieldDesc(name = "出入库类型")
    @Convert(converter = InOutStoreTypeConverter.class)
    private InOutStoreType inOutStoreType;

    @FieldDesc(name = "仓库ID")
    private Long storeId;

    @FieldDesc(name = "备注")
    private String remark;
}
