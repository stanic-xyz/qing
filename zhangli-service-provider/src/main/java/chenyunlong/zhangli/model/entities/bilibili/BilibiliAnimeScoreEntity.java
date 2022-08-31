package chenyunlong.zhangli.model.entities.bilibili;

import chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @TableName bilibili_anime_score
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bilibili_anime_score")
@Data
public class BilibiliAnimeScoreEntity extends BaseEntity<BilibiliAnimeScoreEntity> {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 动漫ID
     */
    private Long animeId;

    /**
     *
     */
    private Double score;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;
}