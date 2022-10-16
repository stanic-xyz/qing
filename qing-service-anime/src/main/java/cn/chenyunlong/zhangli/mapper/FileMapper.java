package cn.chenyunlong.zhangli.mapper;

import cn.chenyunlong.zhangli.model.entities.UploadFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Mapper
@Component
public interface FileMapper extends BaseMapper<UploadFile> {

    /**
     * 插入文件
     *
     * @param uploadFile 文件信息
     */
    @Insert("INSERT INTO `upload_file` (`file_id`, `file_name`, `file_size`, `mime_type`, `url`) VALUES (#{fileId}, #{fileName}, #{fileSize}, #{mimeType}, #{url})")
    void save(UploadFile uploadFile);
}
