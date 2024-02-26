package cn.chenyunlong.qing.domain.productcenter.templateitem;

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
import cn.chenyunlong.qing.infrustructure.converter.ComponentTypeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.type.ComponentType;

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
@Table(name = "brand")
public class TemplateItem extends BaseJpaAggregate {

    @FieldDesc(name = "名称")
    private String name;

    @FieldDesc(name = "编码")
    private String code;

    @FieldDesc(name = "默认值")
    private String defaultValue;

    @FieldDesc(name = "占位符")
    private String placeholder;

    @FieldDesc(name = "创建人")
    private String createUser;

    @FieldDesc(name = "排序号")
    private BigDecimal sortNum;

    @FieldDesc(name = "数据的请求url")
    private String dataUrl;

    @FieldDesc(name = "组件类型")
    @Convert(converter = ComponentTypeConverter.class)
    private ComponentType componentType;

    @FieldDesc(name = "描述")
    private String description;

    @FieldDesc(name = "元数据")
    private String metaData;

    @Convert(converter = ValidStatusConverter.class)
    @IgnoreUpdater
    @IgnoreCreator
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
