package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.Activity;
import chenyunlong.zhangli.mapper.ActivityMapper;
import chenyunlong.zhangli.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Activity> queryActivities() {
        return activityMapper.getActivityList();
    }

    @Override
    public Activity getActivityById(Long activityId) {
        return activityMapper.getActivityById(activityId);
    }
}
