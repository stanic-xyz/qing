package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Stan
 */
@Data
@TableName("anime_recommend")
public class AnimeRecommendEntity extends BaseEntity {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 动漫ID
     */
    private Long aid;

    /**
     * 推荐理由
     */
    private String reason;

    /**
     * 推荐时间
     */
    private LocalDateTime date;

}
