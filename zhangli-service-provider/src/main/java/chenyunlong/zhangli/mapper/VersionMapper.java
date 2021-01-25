package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.Version;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Stan
 * @date 2020/01/26
 */
@Mapper
public interface VersionMapper {
    /**
     * 获取所有版本信息
     *
     * @return 版本信息
     */
    List<Version> getAllVersions();

    /**
     * @param version 版本信息
     */
    void addVersionInfo(Version version);

}
