package chenyunlong.zhangli.model.entities.anime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Stan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "anime_playlist")
public class PlaylistEntity {

    private Long id;
    private Long animeId;
    private String name;
    private String description;
}
