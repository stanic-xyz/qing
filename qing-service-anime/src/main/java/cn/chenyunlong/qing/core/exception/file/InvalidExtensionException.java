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

package cn.chenyunlong.qing.core.exception.file;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import java.util.Arrays;

/**
 * 文件上传 误异常类
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidExtensionException extends FileUploadException {
    private static final long serialVersionUID = 1L;

    private String[] allowedExtension;
    private String extension;
    private String filename;

    public InvalidExtensionException(String[] allowedExtension, String extension, String filename) {
        super("filename : [" + filename + "], extension : [" + extension + "], allowed extension : [" + Arrays.toString(allowedExtension) + "]");
        this.allowedExtension = allowedExtension;
        this.extension = extension;
        this.filename = filename;
    }

    public static class InvalidImageExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        public InvalidImageExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    public static class InvalidFlashExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        public InvalidFlashExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    public static class InvalidMediaExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        public InvalidMediaExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }

    public static class InvalidVideoExtensionException extends InvalidExtensionException {
        private static final long serialVersionUID = 1L;

        public InvalidVideoExtensionException(String[] allowedExtension, String extension, String filename) {
            super(allowedExtension, extension, filename);
        }
    }
}