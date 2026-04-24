package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.LlmParseRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LlmParseRecordRepository extends JpaRepository<LlmParseRecord, Long> {

    Optional<LlmParseRecord> findByTaskId(String taskId);

    List<LlmParseRecord> findByUploadId(String uploadId);

    Optional<LlmParseRecord> findByFileHashAndCategoryStrategy(String fileHash, String categoryStrategy);

    List<LlmParseRecord> findByTaskStatus(String taskStatus);

    @Query("SELECT r FROM LlmParseRecord r WHERE r.taskStatus IN :statuses ORDER BY r.createdAt DESC")
    List<LlmParseRecord> findByTaskStatusIn(@Param("statuses") List<String> statuses);

    boolean existsByFileHashAndCategoryStrategy(String fileHash, String categoryStrategy);

    List<LlmParseRecord> findByFileHash(String fileHash);
}
