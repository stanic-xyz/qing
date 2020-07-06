package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.Activity;

import java.util.List;

public interface ActivityService {

    public List<Activity> queryActivities();

    Activity getActivityById(Long activityId);
}
