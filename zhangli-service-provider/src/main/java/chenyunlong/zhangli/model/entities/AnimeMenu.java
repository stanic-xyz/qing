package chenyunlong.zhangli.model.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class AnimeMenu extends BaseEntity {
    private String id;
    private String name;
    private String path;
}
