package cn.chenyunlong.qing.domain.zan.service;

import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;

public interface IZanService {

    Long createZan(ZanCreator creator);

    void updateZan(ZanUpdater updater);

    void validZan(Long id);

    void invalidZan(Long id);

    Long like(ZanCreateRequest createRequest);
}
