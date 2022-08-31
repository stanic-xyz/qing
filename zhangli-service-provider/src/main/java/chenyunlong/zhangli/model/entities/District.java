package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Stan
 */
@Data
@ToString(callSuper = true)
@TableName("anime_district")
@EqualsAndHashCode(callSuper = true)
public class District extends BaseEntity<District> {
    private Long id;
    private String name;
    private String code;
    private String description;
}
