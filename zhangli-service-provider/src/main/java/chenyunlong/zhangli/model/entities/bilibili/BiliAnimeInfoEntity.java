package chenyunlong.zhangli.model.entities.bilibili;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("bilibili_anime")
public class BiliAnimeInfoEntity {
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
