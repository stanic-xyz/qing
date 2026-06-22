package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.imports.*;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiFileService;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiImportService;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiPreviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/finance/import/qianji")
@RequiredArgsConstructor
public class ImportController {

    private final QianjiImportService importService;
    private final QianjiFileService qianjiFileService;
    private final QianjiPreviewService qianjiPreviewService;

    @PostMapping(value = "/upload")
    public Result<UploadFileResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("accountId") Long accountId) {
        UploadFileResponse response = qianjiFileService.uploadFile(file, accountId);
        return Result.success(response);
    }

    @PostMapping(value = "/checkDuplicate")
    public Result<FileDuplicateCheckResult> checkDuplicate(
            @RequestParam("fileHash") String fileHash,
            @RequestParam("accountId") Long accountId) {
        FileDuplicateCheckResult result = qianjiFileService.checkDuplicate(fileHash, accountId);
        return Result.success(result);
    }

    @GetMapping(value = "/preview")
    public Result<FilePreviewResult> previewFile(@RequestParam Long fileId) {
        FilePreviewResult result = qianjiPreviewService.previewFile(fileId);
        return Result.success(result);
    }

    @PostMapping(value = "/previewAndParse")
    public Result<QianjiPreviewResult> previewAndParse(@RequestParam Long fileId) throws Exception {
        QianjiPreviewResult result = importService.previewByFileId(fileId);
        return Result.success(result);
    }

    @PostMapping(value = "/execute")
    public Result<ImportResult> execute(
            @RequestParam("fileId") Long fileId,
            @RequestPart("request") QianjiImportRequest request) throws Exception {
        ImportResult result = importService.executeByFileId(fileId, request);
        return Result.success(result);
    }

    @PostMapping(value = "/executeRecords", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<ImportResult> executeRecords(@RequestBody QianjiExecuteRecordsRequest body) {
        ImportResult result = importService.executeRecords(body);
        return Result.success(result);
    }

    @GetMapping("/files")
    public Result<Page<UploadFileRecord>> listFiles(
            @RequestParam Long accountId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Page<UploadFileRecord> files = qianjiFileService.listFiles(accountId, page, size);
        return Result.success(files);
    }

    @GetMapping("/files/{fileId}")
    public Result<UploadFileRecord> getFileInfo(@PathVariable Long fileId) {
        UploadFileRecord file = qianjiFileService.getFileInfo(fileId);
        return Result.success(file);
    }

    @DeleteMapping("/files/{fileId}")
    public Result<Void> deleteFile(@PathVariable Long fileId) {
        qianjiFileService.deleteFile(fileId);
        return Result.success(null);
    }
}
