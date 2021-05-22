package chenyunlong.zhangli.model.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class Version extends BaseEntity {
    private Long vid;
    private String code;
    private String name;
    private String description;
}
