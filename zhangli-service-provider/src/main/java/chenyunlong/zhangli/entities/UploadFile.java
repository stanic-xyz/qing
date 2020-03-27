package chenyunlong.zhangli.entities;

import java.io.Serializable;

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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "UploadFile{" +
                "fileID=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", url='" + url + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
