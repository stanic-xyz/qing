package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DraftRecordServiceTest {

    @Mock
    private UnifiedDraftBatchRepository batchRepository;

    @Mock
    private UnifiedDraftRecordRepository draftRecordRepository;

    @InjectMocks
    private DraftRecordService draftRecordService;

    /**
     * 验证分页查询不存在批次时会按资源不存在处理。
     */
    @Test
    @DisplayName("分页查询不存在批次时应抛出资源不存在异常")
    void pageByBatchIdShouldThrowNotFoundWhenBatchMissing() {
        when(batchRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftRecordService.pageByBatchId(1L, 0, 20, null));

        assertTrue(exception.getMessage().contains("草稿批次不存在"));
    }

    /**
     * 验证锁定不存在草稿记录时会按资源不存在处理。
     */
    @Test
    @DisplayName("锁定不存在草稿记录时应抛出资源不存在异常")
    void lockFieldsShouldThrowNotFoundWhenRecordMissing() {
        when(draftRecordRepository.findById(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftRecordService.lockFields(2L, java.util.List.of("amount")));

        assertTrue(exception.getMessage().contains("草稿记录不存在"));
    }

    /**
     * 验证解锁不存在草稿记录时会按资源不存在处理。
     */
    @Test
    @DisplayName("解锁不存在草稿记录时应抛出资源不存在异常")
    void unlockFieldsShouldThrowNotFoundWhenRecordMissing() {
        when(draftRecordRepository.findById(3L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> draftRecordService.unlockFields(3L));

        assertTrue(exception.getMessage().contains("草稿记录不存在"));
    }
}
