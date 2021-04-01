package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * Base entity.
 *
 * @author johnniang
 * @date 3/20/19
 */
@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {

    /**
     * Create time.
     */
    private Date createTime;

    /**
     * Update time.
     */
    private Date updateTime;

    protected void prePersist() {
        Date now = DateUtils.now();
        if (createTime == null) {
            createTime = now;
        }

        if (updateTime == null) {
            updateTime = now;
        }
    }

    protected void preUpdate() {
        updateTime = new Date();
    }

    protected void preRemove() {
        updateTime = new Date();
    }

}
