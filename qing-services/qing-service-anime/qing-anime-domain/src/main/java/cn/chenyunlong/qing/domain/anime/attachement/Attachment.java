/*
 * Copyright (c) 2019-2023  YunLong Chen
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

package cn.chenyunlong.qing.domain.anime.attachement;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.*;

import java.time.Instant;

/**
 * @author 陈云龙
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Attachment extends BaseAggregate {

    @FieldDesc(name = "文件类型", description = "文件描述信息")
    private String mimeType;

    @FieldDesc(name = "文件原始名称")
    private String fileName;

    @FieldDesc(name = "文件大小")
    private Long fileSize;

    @FieldDesc(name = "文件地址", description = "文件上传地址")
    private String path;

    @FieldDesc(name = "存储路径", description = "存储路径（本地存储：绝对路径；云存储：对象URL或Key）")
    private String storagePath;

    @FieldDesc(name = "存储类型")
    private Long storageType;

    @FieldDesc(name = "文件内容哈希值", description = "文件内容哈希值（如MD5、SHA-256），用于检测重复文件")
    private String contentHash;

    @FieldDesc(name = "上传时间", description = "文件上传时间")
    private Instant uploadTime;

}
