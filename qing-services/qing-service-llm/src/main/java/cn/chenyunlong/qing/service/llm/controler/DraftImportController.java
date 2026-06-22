package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.draft.ChangeDraftBatchStatusRequest;
import cn.chenyunlong.qing.service.llm.dto.draft.CreateDraftBatchRequest;
import cn.chenyunlong.qing.service.llm.dto.draft.DraftBatchResponse;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.enums.DraftMatchStatusEnum;
import cn.chenyunlong.qing.service.llm.service.DraftBatchService;
import cn.chenyunlong.qing.service.llm.service.DraftCommitService;
import cn.chenyunlong.qing.service.llm.service.DraftRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import/draft")
@RequiredArgsConstructor
public class DraftImportController {

    private final DraftBatchService draftBatchService;
    private final DraftRecordService draftRecordService;
    private final DraftCommitService draftCommitService;

    @PostMapping("/batches")
    public ResponseEntity<Result<DraftBatchResponse>> createBatch(@RequestBody(required = false) CreateDraftBatchRequest request) {
        CreateDraftBatchRequest safeRequest = request == null ? new CreateDraftBatchRequest() : request;
        return ResponseEntity.ok(Result.success(draftBatchService.create(safeRequest)));
    }

    @GetMapping("/batches/{id}")
    public ResponseEntity<Result<DraftBatchResponse>> getBatch(@PathVariable Long id) {
        return ResponseEntity.ok(Result.success(draftBatchService.get(id)));
    }

    @PostMapping("/batches/{id}/actions")
    public ResponseEntity<Result<DraftBatchResponse>> doAction(@PathVariable Long id,
                                                               @RequestBody ChangeDraftBatchStatusRequest request) {
        return ResponseEntity.ok(Result.success(draftBatchService.changeStatus(id, request.getTargetStatus())));
    }

    @GetMapping("/batches/{id}/records")
    public ResponseEntity<Result<Page<UnifiedDraftRecord>>> pageRecords(@PathVariable Long id,
                                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                                         @RequestParam(value = "size", defaultValue = "20") int size,
                                                                         @RequestParam(value = "matchStatus", required = false) DraftMatchStatusEnum matchStatus) {
        return ResponseEntity.ok(Result.success(draftRecordService.pageByBatchId(id, page, size, matchStatus)));
    }

    @PostMapping("/batches/{id}/commit")
    public ResponseEntity<Result<DraftCommitService.CommitResult>> commitBatch(@PathVariable Long id) {
        DraftCommitService.CommitResult result = draftCommitService.commit(id);
        return ResponseEntity.ok(Result.success(result));
    }

    @PostMapping("/records/{id}/lock")
    public ResponseEntity<Result<UnifiedDraftRecord>> lockRecordFields(@PathVariable Long id,
                                                                      @RequestBody List<String> fields) {
        UnifiedDraftRecord record = draftRecordService.lockFields(id, fields);
        return ResponseEntity.ok(Result.success(record));
    }

    @PostMapping("/records/{id}/unlock")
    public ResponseEntity<Result<UnifiedDraftRecord>> unlockRecordFields(@PathVariable Long id) {
        UnifiedDraftRecord record = draftRecordService.unlockFields(id);
        return ResponseEntity.ok(Result.success(record));
    }
}
