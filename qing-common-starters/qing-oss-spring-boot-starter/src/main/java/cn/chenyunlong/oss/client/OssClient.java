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

package cn.chenyunlong.oss.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.io.InputStream;

/**
 * Oss 基础操作
 * 想要更复杂订单操作可以直接获取AmazonS3，通过AmazonS3 来进行复杂的操作
 * <a href="https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/examples-s3-buckets.html">...</a>
 */
public interface OssClient {
    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 获取url
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return url
     */
    String getObjectURL(String bucketName, String objectName);


    /**
     * 获取存储对象信息
     *
     * @param bucketName bucket名称
     * @param objectName 对象名称
     * @return 对象信息
     */
    S3Object getObjectInfo(String bucketName, String objectName);


    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  对象名称
     * @param stream      文件流
     * @param size        文件大小
     * @param contextType 文件类型
     * @return 对象信息
     * @throws IOException 异常
     */
    PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws IOException;


    default PutObjectResult putObject(String bucketName, String objectName, InputStream stream) throws IOException {
        return putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    AmazonS3 getS3Client();
}
