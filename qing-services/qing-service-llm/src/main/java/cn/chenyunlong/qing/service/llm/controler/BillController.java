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

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@Slf4j
public class BillController {

    @Autowired
    private UploadService uploadService;

    @PostMapping(value = "/upload-batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Result> uploadBillBatch(@RequestParam("files") List<MultipartFile> files,
                                                  @RequestParam("channel") String channel,
                                                  @RequestParam("accountId") Long accountId) {
        try {
            List<cn.chenyunlong.qing.service.llm.dto.UploadBatchPreviewResponse> previews = uploadService.parseAndPreviewBatch(files, channel, accountId);
            return ResponseEntity.ok(Result.success(previews));
        } catch (Exception e) {
            log.error("批量上传解析失败", e);
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    @GetMapping("/preview/{uploadId}")
    public ResponseEntity<Result> getPreview(@PathVariable("uploadId") String uploadId) {
        try {
            return ResponseEntity.ok(Result.success(uploadService.getPreviewData(uploadId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    @PostMapping("/import")
    public ResponseEntity<Result> importBills(@RequestBody ImportRequest request) {
        int count = uploadService.importConfirmed(request);
        return ResponseEntity.ok(Result.success(count));
    }

    @PostMapping("/match/{uploadId}")
    public ResponseEntity<Result> startMatching(@PathVariable("uploadId") String uploadId, @RequestBody(required = false) List<String> lockedTempIds) {
        try {
            uploadService.startMatchingAsync(uploadId, lockedTempIds);
            return ResponseEntity.ok(Result.success(null));
        } catch (Exception e) {
            log.error("启动匹配失败", e);
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    @PostMapping("/match-single/{recordId}")
    public ResponseEntity<Result> matchSingleRecord(@PathVariable("recordId") String recordId, @RequestBody(required = false) List<Long> ruleIds) {
        try {
            return ResponseEntity.ok(Result.success(uploadService.matchSingleRecord(recordId, ruleIds)));
        } catch (Exception e) {
            log.error("单条匹配失败", e);
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }

    @GetMapping("/match/status/{uploadId}")
    public ResponseEntity<Result> getMatchStatus(@PathVariable("uploadId") String uploadId) {
        try {
            return ResponseEntity.ok(Result.success(uploadService.getMatchStatus(uploadId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Result.error(400, e.getMessage()));
        }
    }
}
