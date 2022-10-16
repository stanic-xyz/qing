package cn.chenyunlong.zhangli.service.impl;

import cn.chenyunlong.zhangli.mapper.FileMapper;
import cn.chenyunlong.zhangli.model.entities.UploadFile;
import cn.chenyunlong.zhangli.service.FileUploadService;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final FileMapper fileMapper;

    public FileUploadServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }


    @Override
    public void saveFile(UploadFile uploadFile) {
        fileMapper.save(uploadFile);
    }
}
