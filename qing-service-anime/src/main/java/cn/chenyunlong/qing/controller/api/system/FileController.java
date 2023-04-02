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

package cn.chenyunlong.qing.controller.api.system;

import cn.chenyunlong.qing.domain.file.UploadFile;
import cn.chenyunlong.qing.infrastructure.annotation.Log;
import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import cn.chenyunlong.qing.infrastructure.model.ApiResult;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 文件控制器
 *
 * @author Stan
 * @date 2022/11/05
 */
@Tag(name = "文件控制器")
@Slf4j
@RestController
@RequestMapping("api/sys/file")
@RequiredArgsConstructor
public class FileController {

    private final QingProperties qingProperties;
    private final COSClient cosClient;

    @Log(title = "获取存储桶列表")
    @Operation(summary = "获取存储桶列表")
    @GetMapping("bucket/list")
    public ApiResult<List<Bucket>> listBuckts() {
        List<Bucket> buckets = cosClient.listBuckets();
        return ApiResult.success(buckets);
    }

    /**
     * 上传单张图片
     *
     * @param file 文件
     * @return 返回错误信息了
     * @throws IOException io异常
     */
    @Log
    @Operation(summary = "上传文件", description = "上传文件")
    @PostMapping("upload")
    public ApiResult<String> uploadFile(@RequestBody MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            UploadFile uploadFile = new UploadFile();

            String originalFilename = file.getOriginalFilename();

            String filePath = LocalDate.now().toString();

            String randomUUID = UUID.randomUUID().toString();

            assert originalFilename != null;
            String fileName = filePath + randomUUID + originalFilename.substring(originalFilename.lastIndexOf('.'));

            String baseUploadDir = qingProperties.getFile().getBaseUploadDir();

            File localFile = new File(baseUploadDir + filePath);
            if (!localFile.exists()) {
                boolean mkdirs = localFile.mkdirs();
                if (!mkdirs) {
                    throw new IOException("创建文件" + localFile.getAbsoluteFile() + "失败");
                }
            }
            File file1 = new File(localFile, fileName);
            InputStream fileInputStream = file.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            byte[] b = new byte[1024];
            int len;
            while (((len = fileInputStream.read(b)) != -1)) {
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.close();
            fileInputStream.close();


            uploadFile.setFileName(file.getOriginalFilename());
            uploadFile.setUrl(qingProperties.getFile().getImageServerUrl() + fileName);
            uploadFile.setFileSize(file.getSize());
            uploadFile.setMimeType(file.getContentType());
//            fileUploadService.saveFile(uploadFile);
            return ApiResult.success(fileName);
        } else {
            return ApiResult.fail("没有上传图片啊");
        }
    }

    @Log
    @Operation(summary = "上传文件到腾讯对象存储", description = "上传文件到腾讯对象存储")
    @PostMapping("cos/upload")
    public ApiResult<List<COSObjectSummary>> cosUpload() {
        List<COSObjectSummary> cosObjects = new LinkedList<>();
        List<Bucket> buckets = cosClient.listBuckets();
        for (Bucket bucket : buckets) {
            String bucketName = bucket.getName();
            String bucketLocation = bucket.getLocation();

            log.info("存储桶名称：" + bucketName);
            log.info("存储桶名称：" + bucketLocation);
        }


        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(buckets.get(0).getName());
        // prefix表示列出的object的key以prefix开始
        listObjectsRequest.setPrefix("images/");
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosClientException e) {
                e.printStackTrace();
                return ApiResult.fail(e.getMessage());
            }
            // common prefix表示表示被delimiter截断的路径, 如delimiter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefix = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();
            }

            cosObjects.addAll(cosObjectSummaries);
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());

        return ApiResult.success(cosObjects);
    }
}
