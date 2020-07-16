package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.Activity;

import java.util.List;

public interface ActivityService {


    /**
     * 获取所有动态记录
     *
     * @return
     */
    public List<Activity> queryActivities();

    Activity getActivityById(Long activityId);
}
