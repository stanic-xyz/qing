package chenyunlong.zhangli.model.entities.bilibili;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import chenyunlong.zhangli.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bilibili_anime")
public class BiliAnimeInfoEntity extends BaseEntity {
    @TableId
    private Long id;
    private Long mediaId;
    private String title;
    private Long seasonId;
    private String cover;
    private Integer isFinished;
    private String indexShow;
    private Double score;
    private String link;
}
