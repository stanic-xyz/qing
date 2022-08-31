package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.dto.VersionDTO;
import chenyunlong.zhangli.model.entities.Version;
import chenyunlong.zhangli.model.params.VersionParam;
import chenyunlong.zhangli.service.base.CrudService;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author Stan
 */
public interface VersionService extends CrudService<Version> {
    /**
     * 获取版本列表
     *
     * @return 版本列表
     */
    List<VersionDTO> getAllVersions();

    /**
     * 获取服务详情
     *
     * @param versionId 颁布的信息ID
     * @return 服务代码
     */
    VersionDTO getDetailById(Serializable versionId);

    /**
     * 添加了审批
     *
     * @param param 添加参数
     * @param id    主键ID
     */
    void updateBy(VersionParam param, String id);

    /**
     * 创建服务
     *
     * @param version 创建版本信息
     * @return 备注
     */
    VersionDTO createBy(@NonNull Version version);
}
