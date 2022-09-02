package cn.chenyunlong.zhangli.model.entities.anime;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class PlaylistEntity extends BaseEntity<PlaylistEntity> {

    private Long id;
    private Long animeId;
    private String name;
    private String description;
}
