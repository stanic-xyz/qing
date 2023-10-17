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

package cn.chenyunlong.oss.client;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;

/**
 * s3 是一个协议。
 * S3是Simple Storage Service的缩写，即简单存储服务
 */
@RequiredArgsConstructor
public class S3OssClient implements OssClient {

    private final AmazonS3 amazonS3;


    @Override
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    @Override
    public String getObjectUrl(String bucketName, String objectName) {
        URL url = amazonS3.getUrl(bucketName, objectName);
        return url.toString();
    }

    @Override
    public S3Object getObjectInfo(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream,
                                     long size, String contextType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        PutObjectRequest putObjectRequest =
            new PutObjectRequest(bucketName, objectName, stream, objectMetadata);
        putObjectRequest.getRequestClientOptions().setReadLimit(Long.valueOf(size).intValue() + 1);
        return amazonS3.putObject(putObjectRequest);
    }

    @Override
    public AmazonS3 getS3Client() {
        return amazonS3;
    }
}
