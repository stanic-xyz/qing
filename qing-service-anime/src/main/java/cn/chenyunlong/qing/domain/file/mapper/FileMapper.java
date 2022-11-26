/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.file.mapper;

import cn.chenyunlong.qing.domain.file.UploadFile;
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
