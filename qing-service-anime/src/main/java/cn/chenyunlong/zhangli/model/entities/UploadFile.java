package cn.chenyunlong.zhangli.model.entities;

import cn.chenyunlong.zhangli.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UploadFile extends BaseEntity<UploadFile> {

    @TableId
    private Long fileId;
    private String fileName;
    private String mimeType;
    private String url;
    private Long fileSize;

}
