package cn.chenyunlong.qing.samples.codegen.domain;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import lombok.Data;


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
@Data
@GenMapper
public class TestDomain extends BaseEntity {

    private String username;

    private String password;

    private Integer order;
}
