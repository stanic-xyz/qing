package chenyunlong.zhangli.model.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.stan.zhangli.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("anime_feedback")
@EqualsAndHashCode(callSuper = true)
public class AnimeFeedbackEntity extends BaseEntity {

    @TableId
    private Long id;
    private Long mid;
    private Integer type;
    private String detail;
    private String uid;
    private Integer processingStatus;
}
