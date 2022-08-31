package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.mapper.VersionMapper;
import chenyunlong.zhangli.model.dto.VersionDTO;
import chenyunlong.zhangli.model.entities.Version;
import chenyunlong.zhangli.model.params.VersionParam;
import chenyunlong.zhangli.service.VersionService;
import chenyunlong.zhangli.service.base.AbstractCrudService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * (Version)表服务实现类
 *
 * @author makejava
 * @since 2022-05-21 23:24:50
 */
@Service
public class VersionServiceImpl extends AbstractCrudService<VersionMapper, Version> implements VersionService {

    @Override
    public List<VersionDTO> getAllVersions() {
        return toListDto(lambdaQuery().list(), VersionDTO.class);
    }

    @Override
    public VersionDTO getDetailById(Serializable versionId) {
        return toDto(getById(versionId), VersionDTO.class);
    }

    @Transactional
    @Override
    public void updateBy(VersionParam param, String id) {
        Version version = mustExistById(id);
        param.update(version);
        updateById(version);
    }

    @Override
    public VersionDTO createBy(@NonNull Version version) {
        return toDto(super.create(version), VersionDTO.class);
    }
}

