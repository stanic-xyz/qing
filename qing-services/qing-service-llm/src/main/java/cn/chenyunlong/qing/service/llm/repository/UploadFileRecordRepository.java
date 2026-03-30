package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.UploadFileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadFileRecordRepository extends JpaRepository<UploadFileRecord, Long> {
    Optional<UploadFileRecord> findByFileHash(String fileHash);
    Optional<UploadFileRecord> findByFileName(String fileName);
}
