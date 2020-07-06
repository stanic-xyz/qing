package chenyunlong.zhangli.entities;

import java.io.Serializable;

public class Activity implements Serializable {

    private Long activityId;
    private String activityNmme;


    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityNmme() {
        return activityNmme;
    }

    public void setActivityNmme(String activityNmme) {
        this.activityNmme = activityNmme;
    }
}
