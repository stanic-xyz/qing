package chenyunlong.zhangli.entities;

import java.io.Serializable;

public class Activity implements Serializable {

    private Long activityId;
    private String activityName;


    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
