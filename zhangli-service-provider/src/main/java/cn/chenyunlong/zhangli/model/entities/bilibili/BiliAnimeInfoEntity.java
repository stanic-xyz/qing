package cn.chenyunlong.zhangli.model.entities.bilibili;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bilibili_anime")
public class BiliAnimeInfoEntity extends BaseEntity<BiliAnimeInfoEntity> {
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