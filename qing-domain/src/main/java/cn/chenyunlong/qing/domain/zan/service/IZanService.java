package cn.chenyunlong.qing.domain.zan.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.query.ZanQuery;
import cn.chenyunlong.qing.domain.zan.dto.request.ZanCreateRequest;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.dto.vo.ZanVO;
import org.springframework.data.domain.Page;

public interface IZanService {

    /**
     * create
     */
    Long createZan(ZanCreator creator);

    /**
     * update
     */
    void updateZan(ZanUpdater updater);

    void validZan(Long id);

    void invalidZan(Long id);

    /**
     * findById
     */
    ZanVO findById(Long id);

    /**
     * findByPage
     */
    Page<ZanVO> findByPage(PageRequestWrapper<ZanQuery> query);

    /**
     * 点赞。
     */
    Long like(ZanCreateRequest createRequest);
}
