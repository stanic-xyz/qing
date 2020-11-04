package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.UploadFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Mapper
@Component
public interface FileMapper {

    /**
     * @param uploadFile 文件信息
     */
    @Insert("INSERT INTO `upload_file` (`fileid`, `file_name`, `file_size`, `mime_type`, `url`) VALUES (#{fileId}, #{fileName}, #{fileSize}, #{mimeType}, #{url})")
    void save(UploadFile uploadFile);
}
