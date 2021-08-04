package chenyunlong.zhangli.model.entities.anime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.stan.zhangli.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("anime_list_episode")
public class ListEpisodeEntity extends BaseEntity {
    private Long id;
    private Long listId;
    private Long episodeId;
}
