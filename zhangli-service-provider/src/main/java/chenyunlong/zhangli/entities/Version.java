package chenyunlong.zhangli.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class Version implements Serializable {
    private Long vid;
    private String code;
    private String name;
    private String description;
}
