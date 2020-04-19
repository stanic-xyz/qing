package chenyunlong.zhangli.config;

public class FileProperties {

    private String imageServerUrl;
    private String baseUploadDir;

    public FileProperties() {
    }

    public FileProperties(String imageServerUrl, String baseUploadDir) {
        this.imageServerUrl = imageServerUrl;
        this.baseUploadDir = baseUploadDir;
    }

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }

    public String getBaseUploadDir() {
        return baseUploadDir;
    }

    public void setBaseUploadDir(String baseUploadDir) {
        this.baseUploadDir = baseUploadDir;
    }

    @Override
    public String toString() {
        return "FileConfigurationProperties{" +
                "imageServerUrl='" + imageServerUrl + '\'' +
                ", baseUploadDir='" + baseUploadDir + '\'' +
                '}';
    }
}
