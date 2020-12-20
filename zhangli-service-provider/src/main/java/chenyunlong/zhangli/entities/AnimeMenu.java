package chenyunlong.zhangli.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class AnimeMenu implements Serializable {
    private String id;
    private String name;
    private String path;
}
