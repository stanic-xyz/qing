package chenyunlong.zhangli.common.service.impl;

import chenyunlong.zhangli.model.entities.Version;
import chenyunlong.zhangli.mapper.VersionMapper;
import chenyunlong.zhangli.common.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class VersionServiceImpl implements VersionService {
    @Autowired
    private VersionMapper versionMapper;

    @Override
    public List<Version> getAllVersions() {
        return versionMapper.getAllVersions();
    }
}
