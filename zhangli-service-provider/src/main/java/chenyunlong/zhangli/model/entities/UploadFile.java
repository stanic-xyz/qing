package chenyunlong.zhangli.model.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import chenyunlong.zhangli.core.domain.BaseEntity;
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
