// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.activity.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.activity.creator.ActivityCreator;
import cn.chenyunlong.qing.domain.activity.query.ActivityQuery;
import cn.chenyunlong.qing.domain.activity.updater.ActivityUpdater;
import cn.chenyunlong.qing.domain.activity.vo.ActivityVO;
import org.springframework.data.domain.Page;

public interface IActivityService {
    /**
     * create
     */
    Long createActivity(ActivityCreator creator);

    /**
     * update
     */
    void updateActivity(ActivityUpdater updater);

    /**
     * valid
     */
    void validActivity(Long id);

    /**
     * invalid
     */
    void invalidActivity(Long id);

    /**
     * findById
     */
    ActivityVO findById(Long id);

    /**
     * findByPage
     */
    Page<ActivityVO> findByPage(PageRequestWrapper<ActivityQuery> query);
}