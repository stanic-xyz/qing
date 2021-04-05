package chenyunlong.zhangli.model.entities.anime;

import java.io.Serializable;
import java.security.BasicPermission;
import java.time.LocalDateTime;

import chenyunlong.zhangli.model.entities.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * anime_episode
 *
 * @author Stan
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AnimeEpisodeEntity extends BaseEntity {
    /**
     * 视频ID
     */
    private Long id;

    /**
     * 动漫ID
     */
    private Long animeId;

    /**
     * 播放列表ID
     */
    private Long playlistId;

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
     * 视频上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 视频地址
     */
    private String url1;

    /**
     * 视频播放地址3
     */
    private String url3;

    /**
     * 视频播放地址2
     */
    private String url2;

    /**
     * 视频排序
     */
    private Integer orderNo;
}
