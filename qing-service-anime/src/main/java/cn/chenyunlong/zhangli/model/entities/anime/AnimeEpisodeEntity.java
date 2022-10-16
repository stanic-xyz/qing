package cn.chenyunlong.zhangli.model.entities.anime;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * anime_episode
 *
 * @author Stan
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("anime_episode")
public class AnimeEpisodeEntity extends BaseEntity<AnimeEpisodeEntity> {
    /**
     * 视频ID
     */
    private Long id;

    private Long animeId;

    private Long listId;

    /**
     * 视频标题名称
     */
    private String name;

    /**
     * 视频状态，0正常
     */
    private Integer status;

    /**
     * 上传者名称
     */
    private String uploaderName;

    /**
     * 上传用户ID
     */
    private Long uploaderId;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 视频排序
     */
    private Integer orderNo = 0;
}
