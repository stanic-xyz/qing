package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.Activity;

import java.util.List;

/**
 * @author Stan
 */
public interface ActivityService {


    /**
     * 获取所有动态记录
     *
     * @param keyWorld 活动关键字
     * @return 活动列表
     */
    List<Activity> queryActivitiesByPage(String keyWorld);

    /**
     * 获取活动信息
     *
     * @param activityId 活动信息ID
     * @return 活动信息详情
     */
    Activity getActivityById(Long activityId);

    /**
     * 添加活动信息
     *
     * @param activity 需要添加的活动信息
     */
    void addActivity(Activity activity);

}
