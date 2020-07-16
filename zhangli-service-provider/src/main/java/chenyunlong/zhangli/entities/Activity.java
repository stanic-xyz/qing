package chenyunlong.zhangli.entities;

import java.io.Serializable;

public class Activity implements Serializable {

    private String activityId;

    private String activityName;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
