package cn.chenyunlong.zhangli.model.entities.anime;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("anime_list_episode")
public class ListEpisodeEntity extends BaseEntity<ListEpisodeEntity> {
    private Long id;
    private Long listId;
    private Long episodeId;
}
