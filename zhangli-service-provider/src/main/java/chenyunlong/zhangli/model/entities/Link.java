package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author 陈云龙
 * @date 2021/02/27
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Link extends BaseEntity {
    private Integer id;

    /**
     * Link name.
     */
    private String name;

    /**
     * Link website address.
     */
    private String url;

    /**
     * Website logo.
     */
    private String logo;

    /**
     * Website description.
     */
    private String description;

    /**
     * Link team name.
     */
    private String team;

    /**
     * Sort.
     */
    private Integer priority;
}
