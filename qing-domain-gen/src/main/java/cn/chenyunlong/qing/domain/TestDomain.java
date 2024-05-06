package cn.chenyunlong.qing.domain;

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
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
@GenFeign(serverName = "stanic-feign-client")
@GenController
@GenMapper
@Data
@Entity
public class TestDomain extends BaseEntity {

    private String username;

    private String password;
}
