package chenyunlong.zhangli.model.dto.anime;

import chenyunlong.zhangli.model.dto.PlayListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author Stan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnimeInfoDetailDTO extends AnimeInfoMinimalDTO {
    private List<PlayListDTO> playList;

}
