package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.entities.UploadFile;
import chenyunlong.zhangli.mapper.FileMapper;
import chenyunlong.zhangli.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private FileMapper fileMapper;


    @Override
    public void saveFile(UploadFile uploadFile) {
        fileMapper.save(uploadFile);
    }
}
