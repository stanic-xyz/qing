package chenyunlong.zhangli.entities;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Stan
 */
@Data
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
    private Integer publishState;
}
