package chenyunlong.zhangli.model.entities;

import chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadFile extends BaseEntity {

    @TableId
    private Long fileId;
    private String fileName;
    private String mimeType;
    private String url;
    private Long fileSize;

}
