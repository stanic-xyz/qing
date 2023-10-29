package cn.chenyunlong.qing.domain.zan.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.zan.dto.creator.ZanCreator;
import cn.chenyunlong.qing.domain.zan.dto.query.ZanQuery;
import cn.chenyunlong.qing.domain.zan.dto.updater.ZanUpdater;
import cn.chenyunlong.qing.domain.zan.dto.vo.ZanVO;
import cn.chenyunlong.qing.domain.zan.request.ZanCreateRequest;
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

    /**
     * valid
     */
    void validZan(Long id);

    /**
     * invalid
     */
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
    Long like(ZanCreateRequest creator);
}
