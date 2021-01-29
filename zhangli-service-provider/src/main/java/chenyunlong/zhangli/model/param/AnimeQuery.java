package chenyunlong.zhangli.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeQuery implements Serializable {

    private String keyword = "";
    private String district = "all";
    private String name = "";
    private String version = "all";
    private String year = "all";
    private String status = "all";
    private String type = "all";
    private Integer seasonMonth = -1;
    private String resourceType = "all";
    private String sort = "premiere_date";

}
