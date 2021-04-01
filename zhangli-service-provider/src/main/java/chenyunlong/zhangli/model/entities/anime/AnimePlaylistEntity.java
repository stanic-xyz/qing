package chenyunlong.zhangli.model.entities.anime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Stan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimePlaylistEntity {

    private Long id;
    private String name;
    private String description;
}
