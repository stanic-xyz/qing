package chenyunlong.zhangli.entities.request;

import com.alipay.api.internal.mapping.ApiField;

public class ActivityQueryInfo {

    @ApiField("activity_name")
    public String activityName;


    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
