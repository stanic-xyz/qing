package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.UploadFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface FileMapper {

    @Insert("INSERT INTO `upload_file` (`fileid`, `file_name`, `file_size`, `mime_type`, `url`) VALUES (#{fileId}, #{fileName}, #{fileSize}, #{mimeType}, #{url})")
    void save(UploadFile uploadFile);
}
