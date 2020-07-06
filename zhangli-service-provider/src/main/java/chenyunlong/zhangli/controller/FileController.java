package chenyunlong.zhangli.controller;

import chenyunlong.zhangli.annotation.Log;
import chenyunlong.zhangli.entities.UploadFile;
import chenyunlong.zhangli.properties.ZhangliProperties;
import chenyunlong.zhangli.service.FileUploadService;
import chenyunlong.zhangli.model.response.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.UUID;

@Api
@RestController
@RequestMapping("file")
public class FileController {

    private ZhangliProperties zhangliProperties;
    private FileUploadService fileUploadService;

    @Autowired
    public FileController(ZhangliProperties zhangliProperties, FileUploadService fileUploadService) {
        this.zhangliProperties = zhangliProperties;
        this.fileUploadService = fileUploadService;
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
}
