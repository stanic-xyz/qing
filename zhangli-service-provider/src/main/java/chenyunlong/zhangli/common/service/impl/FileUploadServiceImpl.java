package chenyunlong.zhangli.common.service.impl;

import chenyunlong.zhangli.entities.UploadFile;
import chenyunlong.zhangli.mapper.FileMapper;
import chenyunlong.zhangli.common.service.FileUploadService;
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
