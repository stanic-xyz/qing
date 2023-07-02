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

package cn.chenyunlong.common.utils.file;

import cn.chenyunlong.common.utils.StringUtils;

/**
 * 媒体类型工具类
 *
 * @author ruoyi
 */
public class MimeTypeUtils {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION =
            {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg", "asf", "rm", "rmvb"};

    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 视频格式
            "avi", "bmp", "bz2", "doc", "docx",
            // 图片
            "gif", "gz", "htm", "html", "jpeg", "jpg", "mp4", "pdf", "png",
            // word excel powerpoint
            "ppt", "pptx", "rar", "rmvb", "txt", "xls", "xlsx",
            // 压缩文件
            "zip"};

    public static String getExtension(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return "";
        }
        return switch (prefix) {
            case IMAGE_PNG -> "png";
            case IMAGE_JPG -> "jpg";
            case IMAGE_JPEG -> "jpeg";
            case IMAGE_BMP -> "bmp";
            case IMAGE_GIF -> "gif";
            default -> "";
        };
    }
}
