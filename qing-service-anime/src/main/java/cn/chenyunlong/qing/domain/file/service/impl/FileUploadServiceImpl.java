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

package cn.chenyunlong.qing.domain.file.service.impl;

import cn.chenyunlong.qing.domain.file.UploadFile;
import cn.chenyunlong.qing.domain.file.mapper.FileMapper;
import cn.chenyunlong.qing.domain.file.service.FileUploadService;
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