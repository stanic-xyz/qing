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

package cn.chenyunlong.qing.web.system;

import cn.chenyunlong.qing.domain.file.UploadFile;
import cn.chenyunlong.qing.infrastructure.annotation.Log;
import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import cn.chenyunlong.qing.infrastructure.model.ApiResult;
import cn.chenyunlong.qing.web.system.model.response.TempSecret;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
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

    @Log(title = "获取存储桶列表")
    @Operation(summary = "获取存储桶列表")
    @GetMapping("createTempSecret")
    public ApiResult<TempSecret> getTempSecret() throws IOException {
        TreeMap<String, Object> config = new TreeMap<>();
        //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
        //用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        //用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        // 替换为您的云 api 密钥 SecretId
        config.put("secretId", qingProperties
                .getOss()
                .getSecretId());
        // 替换为您的云 api 密钥 SecretKey
        config.put("secretKey", qingProperties
                .getOss()
                .getSecretKey());

        // 设置域名:
        // 如果您使用了腾讯云 cvm，可以设置内部域名
        //config.put("host", "sts.internal.tencentcloudapi.com");

        // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
        config.put("durationSeconds", 1800);

        // 换成您的 bucket
        config.put("bucket", qingProperties
                .getOss()
                .getBucketName());
        // 换成 bucket 所在地区
        config.put("region", qingProperties
                .getOss()
                .getRegion());

        // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
        // 列举几种典型的前缀授权场景：
        // 1、允许访问所有对象："*"
        // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
        // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
        // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
        config.put("allowPrefixes", new String[]{"*", "exampleobject2"});

        // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
        // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请参见 https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[]{
                // 简单上传
                "name/cos:PutObject",
                // 表单上传、小程序上传
                "name/cos:PostObject",
                // 分块上传
                "name/cos:InitiateMultipartUpload", "name/cos:ListMultipartUploads", "name/cos:ListParts", "name/cos:UploadPart", "name/cos:CompleteMultipartUpload"};
        config.put("allowActions", allowActions);
//        设置condition（如有需要）
        //# 临时密钥生效条件，关于condition的详细设置规则和COS支持的condition类型可以参考 https://cloud.tencent.com/document/product/436/71307
        final String raw_policy = """
                {
                  "version":"2.0",
                  "statement":[
                    {
                      "effect":"allow",
                      "action":[
                          "name/cos:PutObject",
                          "name/cos:PostObject",
                          "name/cos:InitiateMultipartUpload",
                          "name/cos:ListMultipartUploads",
                          "name/cos:ListParts",
                          "name/cos:UploadPart",
                          "name/cos:CompleteMultipartUpload"
                        ],
                      "resource":[
                          "*"
                      ],
                      "condition": {}
                    }
                  ]
                }
                """;

        config.put("policy", raw_policy);
        Response response = CosStsClient.getCredential(config);
        TempSecret tempSecret = new TempSecret();
        tempSecret.setTmpSecretId(response.credentials.tmpSecretId);
        tempSecret.setTmpSecretKey(response.credentials.tmpSecretKey);
        tempSecret.setSessionToken(response.credentials.sessionToken);
        return ApiResult.success(tempSecret);
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
    public ApiResult<String> uploadFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            UploadFile uploadFile = new UploadFile();

            String originalFilename = file.getOriginalFilename();

            String filePath = LocalDate
                    .now()
                    .toString();

            String randomUUID = UUID
                    .randomUUID()
                    .toString();

            assert originalFilename != null;
            String fileName = filePath + randomUUID + originalFilename.substring(originalFilename.lastIndexOf('.'));

            String baseUploadDir = qingProperties
                    .getFile()
                    .getBaseUploadDir();

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
            uploadFile.setUrl(qingProperties
                    .getFile()
                    .getImageServerUrl() + fileName);
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
        listObjectsRequest.setBucketName(buckets
                .get(0)
                .getName());
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
            System.out.println("commonPrefix = " + commonPrefix);

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                System.out.println("key = " + key);
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                System.out.println("etag = " + etag);
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                System.out.println("fileSize = " + fileSize);
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();

                System.out.println("storageClasses = " + storageClasses);
            }

            cosObjects.addAll(cosObjectSummaries);
            String nextMarker = objectListing.getNextMarker();
            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());

        return ApiResult.success(cosObjects);
    }
}
