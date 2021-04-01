package chenyunlong.zhangli.model.entities;

import java.io.Serializable;

public class Attachement implements Serializable {
    private Integer attachementId;
    private String attachementName;
    private String attachementUrl;

    public Attachement() {
    }

    public Attachement(Integer attachementId, String attachementName, String attachementUrl) {
        this.attachementId = attachementId;
        this.attachementName = attachementName;
        this.attachementUrl = attachementUrl;
    }

    public Integer getAttachementId() {
        return attachementId;
    }

    public void setAttachementId(Integer attachementId) {
        this.attachementId = attachementId;
    }

    public String getAttachementName() {
        return attachementName;
    }

    public void setAttachementName(String attachementName) {
        this.attachementName = attachementName;
    }

    public String getAttachementUrl() {
        return attachementUrl;
    }

    public void setAttachementUrl(String attachementUrl) {
        this.attachementUrl = attachementUrl;
    }
}
