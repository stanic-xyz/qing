package chenyunlong.zhangli.model.entities.anime;

import chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;

/**
 * 番剧播放资源(AnimeRecommend)表实体类
 *
 * @author makejava
 * @since 2022-05-22 11:34:04
 */
@Data
public class AnimeRecommend extends BaseEntity<AnimeRecommend> {

    private Long id;
    //番剧ID
    private Long aid;
    //推荐理由
    private String reason;
    //视频排序
    private Integer orderNo;
}

