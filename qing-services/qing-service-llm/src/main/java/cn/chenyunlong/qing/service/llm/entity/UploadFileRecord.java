package cn.chenyunlong.qing.service.llm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "upload_file_record")
@Data
public class UploadFileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileHash;
    private String channel;
    
    private String status; // UPLOADED, IMPORTED, FAILED
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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
