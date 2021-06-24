package chenyunlong.zhangli.model.params;

import chenyunlong.zhangli.model.dto.base.InputConverter;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode
public class AnimeInfoSearchParam {

    private String keyword = "";
    private String sort = "premiere_date";

}
