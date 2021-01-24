package chenyunlong.zhangli.entities;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {
    private String activityId;
    private String username;
    private String activityName;
    private String activityContent;
    private String attachmentAddress;
    private Integer attachmanetType;
    private Attachement attachement;
    private Date createTime;
    private Date modifyTime;
    /**
     * 发表状态0：未发表，1:已发表，
     */
    private Integer publishState;


    public Integer getPublishState() {
        return publishState;
    }

    public void setPublishState(Integer publishState) {
        this.publishState = publishState;
    }

    public String getAttachmentAddress() {
        return attachmentAddress;
    }

    public void setAttachmentAddress(String attachmentAddress) {
        this.attachmentAddress = attachmentAddress;
    }


    public Integer getAttachmanetType() {
        return attachmanetType;
    }

    public void setAttachmanetType(Integer attachmanetType) {
        this.attachmanetType = attachmanetType;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Attachement getAttachement() {
        return attachement;
    }

    public void setAttachement(Attachement attachement) {
        this.attachement = attachement;
    }
}
