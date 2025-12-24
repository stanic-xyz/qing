package cn.chenyunlong.qing.anime.domain.services;

// 领域层：定义文件存储能力契约
public interface FileStorageService {

    /**
     * 上传文件到存储服务
     *
     * @param file       文件内容
     * @param targetPath 目标路径（包含文件名）
     * @return 文件访问URL
     */
    String uploadFile(byte[] file, String targetPath);

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     */
    void deleteFile(String fileUrl);
}
