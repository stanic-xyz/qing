package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.ImportRequest;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.UploadPreview;
import cn.chenyunlong.qing.service.llm.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bills")
@Slf4j
public class BillController {

    @Autowired
    private UploadService uploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Result> uploadBill(@RequestParam("file") MultipartFile file,
                                             @RequestParam("channel") String channel) {
        try {
            UploadPreview preview = uploadService.parseAndPreview(file, channel);
            return ResponseEntity.ok(Result.success(preview));
        } catch (Exception e) {
            log.error("上传解析失败", e);
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    @PostMapping("/import")
    public ResponseEntity<Result> importBills(@RequestBody ImportRequest request) {
        int count = uploadService.importConfirmed(request);
        return ResponseEntity.ok(Result.success(count));
    }
}
