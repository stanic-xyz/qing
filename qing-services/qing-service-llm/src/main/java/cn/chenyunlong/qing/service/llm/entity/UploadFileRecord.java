package cn.chenyunlong.qing.service.llm.entity;

import cn.chenyunlong.qing.service.llm.enums.FileParseStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.FileTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.FileUploadStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "finance_upload_file_record")
@Data
public class UploadFileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileHash;
    private String channel;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Enumerated(EnumType.STRING)
    private FileUploadStatusEnum status;

    @Enumerated(EnumType.STRING)
    private FileTypeEnum fileType;

    @Enumerated(EnumType.STRING)
    private FileParseStatusEnum parseStatus;

    private String filePath;
    private String parseError;
    private String originalData;

    private Integer parsedCount;
    private Integer importedCount;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long fileSize;
    private String templateVersion;

    private LocalDateTime importedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (parseStatus == null) {
            parseStatus = FileParseStatusEnum.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
