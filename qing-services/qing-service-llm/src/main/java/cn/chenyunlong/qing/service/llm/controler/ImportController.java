package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.imports.*;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiFileService;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiImportService;
import cn.chenyunlong.qing.service.llm.service.imports.qianji.QianjiPreviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/finance/import/qianji")
@Slf4j
@RequiredArgsConstructor
public class ImportController {

    private final QianjiImportService importService;
    private final QianjiFileService qianjiFileService;
    private final QianjiPreviewService qianjiPreviewService;

    @PostMapping(value = "/upload")
    public Result<UploadFileResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("accountId") Long accountId) {
        try {
            UploadFileResponse response = qianjiFileService.uploadFile(file, accountId);
            return Result.success(response);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error(500, "上传失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/checkDuplicate")
    public Result<FileDuplicateCheckResult> checkDuplicate(
            @RequestParam("fileHash") String fileHash,
            @RequestParam("accountId") Long accountId) {
        try {
            FileDuplicateCheckResult result = qianjiFileService.checkDuplicate(fileHash, accountId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("文件去重检测失败", e);
            return Result.error(500, "检测失败：" + e.getMessage());
        }
    }

    @GetMapping(value = "/preview")
    public Result<FilePreviewResult> previewFile(@RequestParam Long fileId) {
        try {
            FilePreviewResult result = qianjiPreviewService.previewFile(fileId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("文件预览失败", e);
            return Result.error(500, "预览失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/previewAndParse")
    public Result<QianjiPreviewResult> previewAndParse(@RequestParam Long fileId) {
        try {
            QianjiPreviewResult result = importService.previewByFileId(fileId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("预览解析失败", e);
            return Result.error(500, "预览失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/execute")
    public Result<ImportResult> execute(
            @RequestParam("fileId") Long fileId,
            @RequestPart("request") QianjiImportRequest request) {
        try {
            ImportResult result = importService.executeByFileId(fileId, request);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入失败", e);
            return Result.error(500, "导入失败，请稍后重试");
        }
    }

    @PostMapping(value = "/executeRecords", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<ImportResult> executeRecords(@RequestBody QianjiExecuteRecordsRequest body) {
        try {
            ImportResult result = importService.executeRecords(body);
            return Result.success(result);
        } catch (Exception e) {
            log.error("导入失败", e);
            return Result.error(500, "导入失败，请稍后重试");
        }
    }

    @GetMapping("/files")
    public Result<Page<UploadFileRecord>> listFiles(
            @RequestParam Long accountId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        try {
            Page<UploadFileRecord> files = qianjiFileService.listFiles(accountId, page, size);
            return Result.success(files);
        } catch (Exception e) {
            log.error("获取文件列表失败", e);
            return Result.error(500, "获取文件列表失败");
        }
    }

    @GetMapping("/files/{fileId}")
    public Result<UploadFileRecord> getFileInfo(@PathVariable Long fileId) {
        try {
            UploadFileRecord file = qianjiFileService.getFileInfo(fileId);
            return Result.success(file);
        } catch (Exception e) {
            log.error("获取文件信息失败", e);
            return Result.error(500, "获取文件信息失败");
        }
    }

    @DeleteMapping("/files/{fileId}")
    public Result<Void> deleteFile(@PathVariable Long fileId) {
        try {
            qianjiFileService.deleteFile(fileId);
            return Result.success(null);
        } catch (Exception e) {
            log.error("删除文件失败", e);
            return Result.error(500, "删除文件失败");
        }
    }
}
