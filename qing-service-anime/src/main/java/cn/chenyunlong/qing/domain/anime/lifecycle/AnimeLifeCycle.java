package cn.chenyunlong.qing.domain.anime.lifecycle;

import cn.chenyunlong.codegen.processor.GenBase;
import cn.chenyunlong.codegen.processor.api.*;
import cn.chenyunlong.codegen.processor.controller.GenController;
import cn.chenyunlong.codegen.processor.creator.GenCreator;
import cn.chenyunlong.codegen.processor.mapper.GenMapper;
import cn.chenyunlong.codegen.processor.query.GenQuery;
import cn.chenyunlong.codegen.processor.repository.GenRepository;
import cn.chenyunlong.codegen.processor.service.GenService;
import cn.chenyunlong.codegen.processor.service.GenServiceImpl;
import cn.chenyunlong.codegen.processor.updater.GenUpdater;
import cn.chenyunlong.codegen.processor.vo.GenVo;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta.InOutBizType;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta.InOutBizTypeConverter;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta.InOutType;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.meta.InOutTypeConverter;
import cn.chenyunlong.qing.infrastructure.domain.BaseEntity;
import lombok.Data;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@GenVo
@GenCreator
@GenUpdater
@GenRepository
@GenService
@GenServiceImpl
@GenController
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenFeign
@GenMapper
@GenBase(basePackage = "cn.chenyunlong.qing.domain.anime.lifecycle")
@Entity
@Table(name = "anime_lifecycle")
public class AnimeLifeCycle extends BaseEntity {

    @FieldDesc(name = "资产名称")
    private String name;

    @FieldDesc(name = "资产Id")
    private Long assetsId;

    private Long skuId;

    @FieldDesc(name = "唯一编码")
    private String uniqueCode;

    @FieldDesc(name = "仓库名称")
    private String houseName;

    @FieldDesc(name = "仓库id")
    private Long houseId;

    @FieldDesc(name = "出入库业务类型")
    @Convert(converter = InOutBizTypeConverter.class)
    private InOutBizType inOutBizType;

    @FieldDesc(name = "出入类型")
    @Convert(converter = InOutTypeConverter.class)
    private InOutType inOutType;

    @FieldDesc(name = "操作人")
    private String operateUser;

    @FieldDesc(name = "唯一批次号")
    private String genBatchNo;

    @FieldDesc(name = "批次号")
    private String batchNo;

}
