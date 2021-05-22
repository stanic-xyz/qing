package chenyunlong.zhangli.model.entities;

import lombok.*;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class UploadFile extends BaseEntity {

    private Long fileId;
    private String fileName;
    private String mimeType;
    private String url;
    private Long fileSize;

}
