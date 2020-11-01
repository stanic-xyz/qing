package chenyunlong.zhangli.entities;

import lombok.*;

import java.io.Serializable;

/**
 * @author Stan
 */
@Data
public class UploadFile implements Serializable {

    private Long fileId;
    private String fileName;
    private String mimeType;
    private String url;
    private Long fileSize;

    public UploadFile() {
    }

    public UploadFile(Long fileId, String fileName, String mimeType, String url, Long fileSize) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.url = url;
        this.fileSize = fileSize;
    }

}
