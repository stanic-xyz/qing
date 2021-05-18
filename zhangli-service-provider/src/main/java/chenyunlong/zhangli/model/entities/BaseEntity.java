package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.utils.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Base entity.
 *
 * @author Stan
 * @date 2021/04/03
 */
@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {

    /**
     * Create time.
     */
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    private LocalDateTime updateTime;

    protected void prePersist() {
        LocalDateTime now = DateUtils.now();
        if (createTime == null) {
            createTime = now;
        }

        if (updateTime == null) {
            updateTime = now;
        }
    }

    protected void preUpdate() {
        updateTime = DateUtils.now();
    }

    protected void preRemove() {
        updateTime = DateUtils.now();
    }

}
