package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.annotation.Log;
import chenyunlong.zhangli.entities.UploadFile;
import chenyunlong.zhangli.config.properties.ZhangliProperties;
import chenyunlong.zhangli.service.FileUploadService;
import chenyunlong.zhangli.model.vo.ApiResult;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Api
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {

    private final ZhangliProperties zhangliProperties;
    private final FileUploadService fileUploadService;
    private final COSClient cosClient;

    @Autowired
    public FileController(ZhangliProperties zhangliProperties, FileUploadService fileUploadService, COSClient cosClient) {
        this.zhangliProperties = zhangliProperties;
        this.fileUploadService = fileUploadService;
        this.cosClient = cosClient;
    }

    /**
     * 上传单张图片
     *
     * @param multipartFile 文件
     * @return
     * @throws IOException
     */
    @Log
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @PostMapping("upload")
    public ApiResult uploadFile(@RequestParam MultipartFile multipartFile) throws IOException {

        if (!multipartFile.isEmpty()) {

            UploadFile uploadFile = new UploadFile();

            String originalFilename = multipartFile.getOriginalFilename();

            String filePath = LocalDate.now().toString();

            String randomUUID = UUID.randomUUID().toString();
            assert originalFilename != null;
            String fileName = filePath + randomUUID + originalFilename.substring(originalFilename.lastIndexOf('.'), originalFilename.length());

            String baseUploadDir = zhangliProperties.getFile().getBaseUploadDir();

            File file = new File(baseUploadDir + filePath);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
            }
            File file1 = new File(file, fileName);
            InputStream fileInputStream = multipartFile.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            byte[] b = new byte[1024];
            int len = 0;
            while (((len = fileInputStream.read(b)) != -1)) {
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.close();
            fileInputStream.close();


            uploadFile.setFileName(multipartFile.getOriginalFilename());
            uploadFile.setUrl(zhangliProperties.getFile().getImageServerUrl() + fileName);
            uploadFile.setFileSize(multipartFile.getSize());
            uploadFile.setMimeType(multipartFile.getContentType());
            fileUploadService.saveFile(uploadFile);
            return ApiResult.success("上传成功");
        } else {
            return ApiResult.faild("没有上传图片啊");
        }
    }

    @Log
    @ApiOperation(value = "上传文件到腾讯对象存储", notes = "上传文件到腾讯对象存储")
    @PostMapping("cos/upload")
    public ApiResult cosUpload() {
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
//            listObjectsRequest.setPrefix("images/");
// deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
//            listObjectsRequest.setDelimiter("/");
// 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosClientException e) {
                e.printStackTrace();
                return ApiResult.faild(e.getMessage());
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

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
