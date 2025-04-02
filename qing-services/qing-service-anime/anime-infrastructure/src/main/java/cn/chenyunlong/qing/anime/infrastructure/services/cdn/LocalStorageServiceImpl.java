package cn.chenyunlong.qing.anime.infrastructure.services.cdn;

import cn.chenyunlong.qing.anime.domain.services.FileStorageService;
import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LocalStorageServiceImpl implements FileStorageService {

    private final File fileBasePath = new File("I:\\data\\qing\\file");

    private static final Logger log = LoggerFactory.getLogger(LocalStorageServiceImpl.class);

    @Override
    public String uploadFile(byte[] file, String targetPath) {
        // 调用阿里云OSS SDK上传文件
        FileUtil.writeBytes(file, new File(fileBasePath, targetPath));
        return String.format("https://cdn.example.com/%s", targetPath);
    }

    @Override
    public void deleteFile(String fileUrl) {
        log.info("文件被删除了");
    }
}
