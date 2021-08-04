package chenyunlong.zhangli.model.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import chenyunlong.zhangli.core.domain.BaseEntity;
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
