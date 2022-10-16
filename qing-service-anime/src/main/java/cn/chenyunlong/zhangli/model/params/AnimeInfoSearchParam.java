package cn.chenyunlong.zhangli.model.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeInfoSearchParam {

    private String keyword = "";
    private String sort = "premiere_date";

}
