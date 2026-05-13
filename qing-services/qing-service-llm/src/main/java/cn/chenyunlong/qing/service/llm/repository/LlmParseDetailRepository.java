package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.LlmParseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlmParseDetailRepository extends JpaRepository<LlmParseDetail, Long> {

    List<LlmParseDetail> findByParseRecordId(Long parseRecordId);

    List<LlmParseDetail> findByParseRecordIdAndParseStatus(Long parseRecordId, String parseStatus);

    List<LlmParseDetail> findByParseRecordIdAndNeedReview(Long parseRecordId, Boolean needReview);

    List<LlmParseDetail> findByTransactionRecordId(Long transactionRecordId);

    @Query("SELECT d FROM LlmParseDetail d WHERE d.parseRecordId = :parseRecordId AND d.importStatus = :importStatus")
    List<LlmParseDetail> findByParseRecordIdAndImportStatus(@Param("parseRecordId") Long parseRecordId,
                                                            @Param("importStatus") String importStatus);

    @Query("SELECT d FROM LlmParseDetail d WHERE d.id IN :ids")
    List<LlmParseDetail> findByIdIn(@Param("ids") List<Long> ids);

    long countByParseRecordId(Long parseRecordId);

    long countByParseRecordIdAndParseStatus(Long parseRecordId, String parseStatus);

    @Query("SELECT COUNT(d) FROM LlmParseDetail d WHERE d.parseRecordId = :parseRecordId AND d.confidence < :threshold")
    long countByParseRecordIdAndConfidenceLessThan(@Param("parseRecordId") Long parseRecordId,
                                                   @Param("threshold") java.math.BigDecimal threshold);

    void deleteByParseRecordId(Long parseRecordId);
}
