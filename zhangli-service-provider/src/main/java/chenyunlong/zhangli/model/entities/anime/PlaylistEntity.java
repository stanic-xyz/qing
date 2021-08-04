package chenyunlong.zhangli.model.entities.anime;

import com.baomidou.mybatisplus.annotation.TableName;
import chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "anime_playlist")
public class PlaylistEntity extends BaseEntity {

    private Long id;
    private Long animeId;
    private String name;
    private String description;
}
