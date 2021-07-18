package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Version extends BaseEntity {
    @TableId
    private Long vid;
    private String code;
    private String name;
    private String description;
}
