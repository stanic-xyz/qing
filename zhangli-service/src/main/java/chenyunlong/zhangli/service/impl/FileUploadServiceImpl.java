package chenyunlong.zhangli.service.impl;

import chenyunlong.zhangli.dao.FileRepository;
import chenyunlong.zhangli.entities.UploadFile;
import chenyunlong.zhangli.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final FileRepository fileRepository;

    @Autowired
    public FileUploadServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public UploadFile saveFile(UploadFile uploadFile) {
        return fileRepository.save(uploadFile);
    }
}
