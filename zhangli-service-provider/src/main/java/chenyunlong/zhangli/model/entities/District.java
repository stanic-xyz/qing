package chenyunlong.zhangli.model.entities;

import lombok.*;

/**
 * @author Stan
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class District extends BaseEntity {
    private Long id;
    private String name;
    private String code;
    private String description;
}
