package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("anime_feedback")
@EqualsAndHashCode(callSuper = true)
public class AnimeFeedbackEntity extends BaseEntity<AnimeFeedbackEntity> {

    @TableId
    private Long id;
    private Long mid;
    private Integer type;
    private String detail;
    private String uid;
    private Integer processingStatus;
}
