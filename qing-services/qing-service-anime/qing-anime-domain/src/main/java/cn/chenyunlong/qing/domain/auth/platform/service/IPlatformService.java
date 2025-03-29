package cn.chenyunlong.qing.domain.auth.platform.service;

import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;

public interface IPlatformService {

    /**
     * create
     */
    Long createPlatform(PlatformCreator creator);

    /**
     * update
     */
    void updatePlatform(PlatformUpdater updater);

    void validPlatform(Long id);

    void invalidPlatform(Long id);

}
