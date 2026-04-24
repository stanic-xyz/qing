package cn.chenyunlong.qing.service.llm.repository;

import cn.chenyunlong.qing.service.llm.entity.LlmParseDetailChangelog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LlmParseDetailChangelogRepository extends JpaRepository<LlmParseDetailChangelog, Long> {

    List<LlmParseDetailChangelog> findByParseDetailIdOrderByChangedAtDesc(Long parseDetailId);

    List<LlmParseDetailChangelog> findByParseDetailIdAndFieldNameOrderByChangedAtDesc(Long parseDetailId, String fieldName);

    List<LlmParseDetailChangelog> findByChangedByOrderByChangedAtDesc(String changedBy);

    void deleteByParseDetailId(Long parseDetailId);
}
