package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.Activity;
import chenyunlong.zhangli.mapper.ActivityMapper;
import chenyunlong.zhangli.service.ActivityService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }


    @Override
    public List<Activity> queryActivities() {
        return activityMapper.getActivityList();
    }

    @Override
    public Activity getActivityById(Long activityId) {
        return activityMapper.getActivityById(activityId);
    }

    @Override
    public void addActivity(Activity activity) {
        activityMapper.insert(activity);
    }
}
