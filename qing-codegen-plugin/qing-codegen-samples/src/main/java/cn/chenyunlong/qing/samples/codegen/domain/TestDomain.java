package cn.chenyunlong.qing.samples.codegen.domain;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import cn.chenyunlong.qing.samples.codegen.Constants;
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
@GenRepository(sourcePath = Constants.GEN_API_SOURCE)
@GenService(sourcePath = Constants.GEN_API_SOURCE)
@GenServiceImpl(sourcePath = Constants.GEN_API_SOURCE)
@GenFeign(sourcePath = Constants.GEN_API_SOURCE, serverName = "stanic")
@GenController(sourcePath = Constants.GEN_API_SOURCE)
@Data
@GenMapper
@Entity
public class TestDomain extends BaseEntity {

    private String username;

    private String password;
}

