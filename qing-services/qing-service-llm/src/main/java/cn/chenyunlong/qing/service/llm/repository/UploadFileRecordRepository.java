package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadFileRecordRepository extends JpaRepository<UploadFileRecord, Long> {
    Optional<UploadFileRecord> findByFileHash(String fileHash);
    Optional<UploadFileRecord> findByFileName(String fileName);

    Optional<UploadFileRecord> findByFileHashAndAccountId(String fileHash, Long accountId);

    boolean existsByFileHashAndAccountId(String fileHash, Long accountId);

    @Query("SELECT u FROM UploadFileRecord u WHERE u.account.id = :accountId ORDER BY u.createdAt DESC")
    Page<UploadFileRecord> findByAccountIdOrderByCreatedAtDesc(@Param("accountId") Long accountId, Pageable pageable);

    List<UploadFileRecord> findByAccountId(Long accountId);
}
