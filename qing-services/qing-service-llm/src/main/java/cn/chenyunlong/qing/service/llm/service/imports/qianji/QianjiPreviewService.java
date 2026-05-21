package cn.chenyunlong.qing.service.llm.service.imports.qianji;

import cn.chenyunlong.qing.service.llm.dto.imports.FilePreviewResult;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.enums.FileTypeEnum;
import cn.hutool.core.io.FileUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QianjiPreviewService {

    private static final int DEFAULT_PREVIEW_ROWS = 100;
    private static final int MAX_PREVIEW_ROWS = 500;

    private final QianjiFileService qianjiFileService;

    public FilePreviewResult previewFile(Long fileId) {
        return previewFile(fileId, DEFAULT_PREVIEW_ROWS);
    }

    public FilePreviewResult previewFile(Long fileId, int maxRows) {
        UploadFileRecord fileRecord = qianjiFileService.getFileInfo(fileId);

        FilePreviewResult result = new FilePreviewResult();
        result.setFileId(fileRecord.getId());
        result.setFileName(fileRecord.getFileName());
        result.setFileType(fileRecord.getFileType());

        if (maxRows <= 0 || maxRows > MAX_PREVIEW_ROWS) {
            maxRows = DEFAULT_PREVIEW_ROWS;
        }

        try {
            return switch (fileRecord.getFileType()) {
                case CSV -> previewCsv(fileRecord, result, maxRows);
                case EXCEL -> previewExcel(fileRecord, result, maxRows);
                case PDF -> buildPdfPreview(result, fileRecord);
                default -> buildUnsupportedPreview(result);
            };
        } catch (Exception e) {
            log.error("预览文件失败: fileId={}", fileId, e);
            result.setHeaders(List.of("错误"));
            result.setRows(List.of(List.of("预览失败: " + e.getMessage())));
            result.setTotalRows(0);
            return result;
        }
    }

    private FilePreviewResult previewCsv(UploadFileRecord fileRecord, FilePreviewResult result, int maxRows) throws Exception {
        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(qianjiFileService.getFileInputStream(fileRecord.getId()), StandardCharsets.UTF_8))
                .build()) {

            List<String> headers = new ArrayList<>();
            List<List<String>> rows = new ArrayList<>();
            String[] line;
            int rowCount = 0;
            int lineNumber = 0;

            while ((line = reader.readNext()) != null) {
                lineNumber++;

                if (lineNumber == 1) {
                    headers.addAll(Arrays.asList(line));
                    continue;
                }

                if (rowCount >= maxRows) {
                    break;
                }

                List<String> row = new ArrayList<>();
                for (String cell : line) {
                    row.add(cell != null ? cell.trim() : "");
                }
                rows.add(row);
                rowCount++;
            }

            result.setHeaders(headers);
            result.setRows(rows);
            result.setTotalRows(lineNumber - 1);
            result.setDownloadUrl(null);
        }

        return result;
    }

    private FilePreviewResult previewExcel(UploadFileRecord fileRecord, FilePreviewResult result, int maxRows) {
        result.setHeaders(List.of("提示"));
        result.setRows(List.of(List.of("Excel 预览功能开发中，请下载后查看")));
        result.setTotalRows(0);
        result.setDownloadUrl("/api/finance/import/qianji/files/" + fileRecord.getId() + "/download");
        return result;
    }

    private FilePreviewResult buildPdfPreview(FilePreviewResult result, UploadFileRecord fileRecord) {
        result.setHeaders(List.of("提示"));
        result.setRows(List.of(List.of("PDF 文件不支持在线预览")));
        result.setTotalRows(0);
        result.setDownloadUrl("/api/finance/import/qianji/files/" + fileRecord.getId() + "/download");
        return result;
    }

    private FilePreviewResult buildUnsupportedPreview(FilePreviewResult result) {
        result.setHeaders(List.of("提示"));
        result.setRows(List.of(List.of("不支持预览此文件类型")));
        result.setTotalRows(0);
        return result;
    }
}
