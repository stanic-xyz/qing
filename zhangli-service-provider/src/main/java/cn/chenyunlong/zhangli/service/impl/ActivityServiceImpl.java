package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.model.entities.ActivityEntity;
import cn.chenyunlong.zhangli.mapper.ActivityMapper;
import cn.chenyunlong.zhangli.service.ActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Stan
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }


    @Override
    public List<ActivityEntity> queryActivitiesByPage(String keyWorld) {
        return activityMapper.getActivityList();
    }

    @Override
    public ActivityEntity getActivityById(Long activityId) {
        return activityMapper.getActivityById(activityId);
    }

    @Override
    public void addActivity(ActivityEntity activity) {
        activityMapper.insert(activity);
    }
}
