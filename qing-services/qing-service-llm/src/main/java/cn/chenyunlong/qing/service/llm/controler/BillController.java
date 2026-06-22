package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.*;
import cn.chenyunlong.qing.service.llm.service.UploadService;
import jakarta.annotation.Resource;
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

    @Resource
    private UploadService uploadService;

    @PostMapping(value = "/upload-batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Result<List<UploadBatchPreviewResponse>>> uploadBillBatch(@RequestParam("files") List<MultipartFile> files,
                                                                                    @RequestParam(value = "parserId") Long parserId,
                                                                                    @RequestParam("accountId") Long accountId) throws Exception {
        // 兼容旧逻辑
        List<UploadBatchPreviewResponse> previews = uploadService.parseAndPreviewBatch(files, parserId, accountId);
        return ResponseEntity.ok(Result.success(previews));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Result<List<UploadBatchPreviewResponse>>> uploadBill(@RequestParam("file") MultipartFile file,
                                                                               @RequestParam(value = "parserId") Long parserId,
                                                                               @RequestParam("accountId") Long accountId) throws Exception {
        // 兼容旧逻辑
        List<UploadBatchPreviewResponse> previews = uploadService.parseAndPreview(file, parserId, accountId);
        return ResponseEntity.ok(Result.success(previews));
    }

    @GetMapping("/preview/{uploadId}")
    public ResponseEntity<Result<UploadPreview>> getPreview(
            @PathVariable Long uploadId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        UploadPreview previewData = uploadService.getPreviewData(uploadId, page, size);
        return ResponseEntity.ok(Result.success(previewData));
    }

    @PostMapping("/import")
    public ResponseEntity<Result<Integer>> importBills(@RequestBody ImportRequest request) {
        int count = uploadService.importConfirmed(request);
        return ResponseEntity.ok(Result.success(count));
    }

    @PostMapping("/match/{uploadId}")
    public ResponseEntity<Result<Void>> startMatching(@PathVariable("uploadId") Long uploadId, @RequestBody(required = false) List<Long> lockedTempIds) {
        uploadService.startMatchingAsync(uploadId, lockedTempIds);
        return ResponseEntity.ok(Result.success(null));
    }

    @PostMapping("/match-single/{recordId}")
    public ResponseEntity<Result<PreviewRecordDTO>> matchSingleRecord(@PathVariable("recordId") String recordId, @RequestBody(required = false) List<Long> ruleIds) {
        PreviewRecordDTO recordDTO = uploadService.matchSingleRecord(recordId, ruleIds);
        return ResponseEntity.ok(Result.success(recordDTO));
    }

    @GetMapping("/match/status/{uploadId}")
    public ResponseEntity<Result<MatchStatusResponse>> getMatchStatus(@PathVariable("uploadId") Long uploadId) {
        MatchStatusResponse matchStatus = uploadService.getMatchStatus(uploadId);
        return ResponseEntity.ok(Result.success(matchStatus));
    }

    @GetMapping("/batch/overview/{uploadId}")
    public ResponseEntity<Result<UploadBatchOverviewResponse>> getBatchOverview(@PathVariable("uploadId") Long uploadId) {
        UploadBatchOverviewResponse batchOverview = uploadService.getBatchOverview(uploadId);
        return ResponseEntity.ok(Result.success(batchOverview));
    }
}
