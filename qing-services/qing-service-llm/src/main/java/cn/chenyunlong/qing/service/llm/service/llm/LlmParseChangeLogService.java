package cn.chenyunlong.qing.service.llm.service.llm;

import cn.chenyunlong.qing.service.llm.entity.LlmParseDetail;
import cn.chenyunlong.qing.service.llm.entity.LlmParseDetailChangelog;
import cn.chenyunlong.qing.service.llm.repository.LlmParseDetailChangelogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * LLM 解析变更日志服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmParseChangeLogService {

    private final LlmParseDetailChangelogRepository changelogRepository;

    /**
     * 记录变更
     */
    @Transactional
    public void logChange(Long parseDetailId,
                          String fieldName,
                          Object oldValue,
                          Object newValue,
                          String changeSource,
                          String changedBy,
                          String changeReason) {
        LlmParseDetailChangelog changelog = new LlmParseDetailChangelog();
        changelog.setParseDetailId(parseDetailId);
        changelog.setFieldName(fieldName);
        changelog.setOldValue(oldValue != null ? oldValue.toString() : null);
        changelog.setNewValue(newValue != null ? newValue.toString() : null);
        changelog.setChangeSource(changeSource);
        changelog.setChangedBy(changedBy);
        changelog.setChangeReason(changeReason);

        changelogRepository.save(changelog);
        log.debug("Logged change for detail {} field {}: {} -> {}",
                parseDetailId, fieldName, oldValue, newValue);
    }

    /**
     * 记录用户变更
     */
    @Transactional
    public void logUserChange(Long parseDetailId, String fieldName,
                               Object oldValue, Object newValue,
                               String changedBy, String reason) {
        logChange(parseDetailId, fieldName, oldValue, newValue,
                "USER", changedBy, reason);
    }

    /**
     * 记录系统变更
     */
    @Transactional
    public void logSystemChange(Long parseDetailId, String fieldName,
                                 Object oldValue, Object newValue, String reason) {
        logChange(parseDetailId, fieldName, oldValue, newValue,
                "SYSTEM", "system", reason);
    }

    /**
     * 记录 LLM 变更
     */
    @Transactional
    public void logLlmChange(Long parseDetailId, String fieldName,
                              Object oldValue, Object newValue, String reason) {
        logChange(parseDetailId, fieldName, oldValue, newValue,
                "LLM", "llm", reason);
    }

    /**
     * 获取变更历史
     */
    public List<LlmParseDetailChangelog> getHistory(Long parseDetailId) {
        return changelogRepository.findByParseDetailIdOrderByChangedAtDesc(parseDetailId);
    }

    /**
     * 获取特定字段的变更历史
     */
    public List<LlmParseDetailChangelog> getFieldHistory(Long parseDetailId, String fieldName) {
        return changelogRepository.findByParseDetailIdAndFieldNameOrderByChangedAtDesc(parseDetailId, fieldName);
    }
}
