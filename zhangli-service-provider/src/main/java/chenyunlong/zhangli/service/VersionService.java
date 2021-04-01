package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.entities.Version;

import java.util.List;

/**
 * @author Stan
 */
public interface VersionService {
    /**
     * 获取版本列表
     *
     * @return 版本列表
     */
    List<Version> getAllVersions();
}
