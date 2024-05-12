package cn.chenyunlong.qing.domain.auth.platform.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.auth.platform.dto.creator.PlatformCreator;
import cn.chenyunlong.qing.domain.auth.platform.dto.query.PlatformQuery;
import cn.chenyunlong.qing.domain.auth.platform.dto.updater.PlatformUpdater;
import cn.chenyunlong.qing.domain.auth.platform.dto.vo.PlatformVO;
import org.springframework.data.domain.Page;

public interface IPlatformService {

    /**
     * create
     */
    Long createPlatform(PlatformCreator creator);

    /**
     * update
     */
    void updatePlatform(PlatformUpdater updater);

    /**
     * valid
     */
    void validPlatform(Long id);

    /**
     * invalid
     */
    void invalidPlatform(Long id);

    /**
     * findById
     */
    PlatformVO findById(Long id);

    /**
     * findByPage
     */
    Page<PlatformVO> findByPage(PageRequestWrapper<PlatformQuery> query);
}
