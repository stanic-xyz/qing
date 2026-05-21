package cn.chenyunlong.qing.service.llm.service.imports.qianji;

import cn.chenyunlong.qing.service.llm.dto.imports.FileDuplicateCheckResult;
import cn.chenyunlong.qing.service.llm.dto.imports.UploadFileResponse;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import cn.chenyunlong.qing.service.llm.enums.FileParseStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.FileTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.FileUploadStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.UploadFileRecordRepository;
import cn.chenyunlong.qing.service.llm.util.FileHashUtil;
import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class QianjiFileService {

    private final UploadFileRecordRepository uploadFileRecordRepository;
    private final AccountRepository accountRepository;

    @Value("${qianji.file.storage.path:/data/qianji-files}")
    private String storagePath;

    @Value("${qianji.file.max-size:20971520}")
    private long maxFileSize;

    @Value("${qianji.file.allowed-types:csv,xlsx,xls}")
    private String allowedTypes;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("csv", "xlsx", "xls");

    @Transactional
    public UploadFileResponse uploadFile(MultipartFile file, Long accountId) {
        validateFile(file);

        String fileHash;
        try {
            fileHash = FileHashUtil.calcMD5(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("计算文件哈希失败", e);
        }
        Optional<UploadFileRecord> existingFile = uploadFileRecordRepository.findByFileHashAndAccountId(fileHash, accountId);

        if (existingFile.isPresent()) {
            UploadFileRecord existing = existingFile.get();
            UploadFileResponse response = new UploadFileResponse();
            response.setFileId(existing.getId());
            response.setFileName(existing.getFileName());
            response.setFileHash(existing.getFileHash());
            response.setFileSize(existing.getFileSize());
            response.setFileType(existing.getFileType());
            response.setDuplicate(true);
            response.setExistingFileId(existing.getId());
            return response;
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("账户不存在: " + accountId));

        FileTypeEnum fileType = determineFileType(file.getOriginalFilename());

        String storedPath = saveFile(file, accountId, fileHash);

        UploadFileRecord record = new UploadFileRecord();
        record.setFileName(file.getOriginalFilename());
        record.setFileHash(fileHash);
        record.setFileSize(file.getSize());
        record.setFileType(fileType);
        record.setFilePath(storedPath);
        record.setAccount(account);
        record.setStatus(FileUploadStatusEnum.UPLOADED);
        record.setParseStatus(FileParseStatusEnum.PENDING);

        UploadFileRecord saved = uploadFileRecordRepository.save(record);

        UploadFileResponse response = new UploadFileResponse();
        response.setFileId(saved.getId());
        response.setFileName(saved.getFileName());
        response.setFileHash(saved.getFileHash());
        response.setFileSize(saved.getFileSize());
        response.setFileType(saved.getFileType());
        response.setDuplicate(false);
        response.setExistingFileId(null);
        return response;
    }

    public FileDuplicateCheckResult checkDuplicate(String fileHash, Long accountId) {
        Optional<UploadFileRecord> existingFile = uploadFileRecordRepository.findByFileHashAndAccountId(fileHash, accountId);

        FileDuplicateCheckResult result = new FileDuplicateCheckResult();
        if (existingFile.isPresent()) {
            UploadFileRecord file = existingFile.get();
            result.setDuplicate(true);
            result.setExistingFileId(file.getId());
            result.setExistingFileName(file.getFileName());
            result.setExistingUploadTime(file.getCreatedAt());
        } else {
            result.setDuplicate(false);
        }
        return result;
    }

    public UploadFileRecord getFileInfo(Long fileId) {
        return uploadFileRecordRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("文件不存在: " + fileId));
    }

    public InputStream getFileInputStream(Long fileId) {
        UploadFileRecord record = getFileInfo(fileId);
        try {
            Path filePath = Paths.get(record.getFilePath());
            return Files.newInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException("读取文件失败: " + record.getFileName(), e);
        }
    }

    @Transactional
    public void deleteFile(Long fileId) {
        UploadFileRecord record = getFileInfo(fileId);

        Path filePath = Paths.get(record.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            log.warn("删除物理文件失败: {}", record.getFilePath(), e);
        }

        uploadFileRecordRepository.delete(record);
    }

    public Page<UploadFileRecord> listFiles(Long accountId, int page, int size) {
        return uploadFileRecordRepository.findByAccountIdOrderByCreatedAtDesc(accountId, PageRequest.of(page, size));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("文件大小超过限制: " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String extension = FileUtil.extName(file.getOriginalFilename());
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("不支持的文件类型: " + extension);
        }
    }

    private FileTypeEnum determineFileType(String filename) {
        String extension = FileUtil.extName(filename);
        if (extension == null) {
            return FileTypeEnum.OTHER;
        }
        return switch (extension.toLowerCase()) {
            case "csv" -> FileTypeEnum.CSV;
            case "xlsx", "xls" -> FileTypeEnum.EXCEL;
            case "pdf" -> FileTypeEnum.PDF;
            default -> FileTypeEnum.OTHER;
        };
    }

    private String saveFile(MultipartFile file, Long accountId, String fileHash) {
        try {
            Path accountDir = Paths.get(storagePath, String.valueOf(accountId));
            Files.createDirectories(accountDir);

            String extension = FileUtil.extName(file.getOriginalFilename());
            String storedFileName = fileHash + "." + extension;
            Path targetPath = accountDir.resolve(storedFileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }

            return targetPath.toString();
        } catch (Exception e) {
            throw new RuntimeException("保存文件失败", e);
        }
    }
}
