
package chenyunlong.zhangli.model.agefans;

import lombok.Data;
@Data
public class UpdateInfo {

    private String mId;
    private Boolean mIsnew;
    private String mMtime;
    private String mName;
    private String mNamefornew;
    private Long mWd;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Boolean getIsnew() {
        return mIsnew;
    }

    public void setIsnew(Boolean isnew) {
        mIsnew = isnew;
    }

    public String getMtime() {
        return mMtime;
    }

    public void setMtime(String mtime) {
        mMtime = mtime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNamefornew() {
        return mNamefornew;
    }

    public void setNamefornew(String namefornew) {
        mNamefornew = namefornew;
    }

    public Long getWd() {
        return mWd;
    }

    public void setWd(Long wd) {
        mWd = wd;
    }

}
