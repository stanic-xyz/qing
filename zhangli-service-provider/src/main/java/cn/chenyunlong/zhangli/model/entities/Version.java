package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("anime_version")
public class Version extends BaseEntity<Version> {
    @TableId
    private Long vid;
    private String code;
    private String name;
    private String description;
}
