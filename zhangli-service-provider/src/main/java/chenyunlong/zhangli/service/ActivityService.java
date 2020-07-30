package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.Activity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {


    /**
     * 获取所有动态记录
     *
     * @return
     */
    public List<Activity> queryActivitiesByPage(String keyWorld);

    Activity getActivityById(Long activityId);

    void addActivity(Activity activity);

}
