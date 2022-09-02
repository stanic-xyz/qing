package cn.chenyunlong.zhangli.model.vo.anime;

import cn.chenyunlong.zhangli.model.entities.anime.AnimeEpisodeEntity;
import cn.chenyunlong.zhangli.model.dto.base.OutputConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimeEpisodeVo implements OutputConverter<AnimeEpisodeVo, AnimeEpisodeEntity> {
    /**
     * 视频ID
     */
    private Long id;

    /**
     * 动漫ID
     */
    private Long animeId;
    private Long playlistId;
    private String name;

    /**
     * 视频状态，0正常
     */
    private Integer status;

    /**
     * 上传者名称
     */
    private String uploadderName;

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