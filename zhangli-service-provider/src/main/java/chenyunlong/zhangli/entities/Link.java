package chenyunlong.zhangli.entities;

import lombok.Data;

/**
 * @author 陈云龙
 * @date 2021/02/27
 **/
@Data
public class Link {
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
