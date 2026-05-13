package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.draft.CreateDraftBatchRequest;
import cn.chenyunlong.qing.service.llm.dto.draft.DraftBatchResponse;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.enums.AdapterTypeEnum;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DraftBatchServiceTest {

    @Mock
    private UnifiedDraftBatchRepository batchRepository;

    @InjectMocks
    private DraftBatchService draftBatchService;

    private UnifiedDraftBatch batch;

    @BeforeEach
    void setUp() {
        batch = new UnifiedDraftBatch();
        batch.setId(1L);
        batch.setBatchNo("udb-2026-05-07-1001");
        batch.setAdapterType(AdapterTypeEnum.PARSER);
        batch.setStatus(DraftBatchStatusEnum.DRAFTED);
        batch.setProgress(0);
        batch.setTotalRecords(10);
    }

    @Test
    void create_shouldReturnDraftedBatchWithStartMatchAction() {
        when(batchRepository.save(any(UnifiedDraftBatch.class))).thenAnswer(invocation -> {
            UnifiedDraftBatch saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        CreateDraftBatchRequest req = new CreateDraftBatchRequest();
        req.setAdapterType(AdapterTypeEnum.LLM);
        req.setTotalRecords(8);

        DraftBatchResponse result = draftBatchService.create(req);

        assertNotNull(result.getId());
        assertEquals(AdapterTypeEnum.LLM, result.getAdapterType());
        assertEquals(DraftBatchStatusEnum.DRAFTED, result.getStatus());
        assertTrue(result.getAllowedActions().contains("START_MATCH"));
    }

    @Test
    void changeStatus_shouldAllowLegalTransitionAndUpdateProgress() {
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(batchRepository.save(any(UnifiedDraftBatch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DraftBatchResponse matching = draftBatchService.changeStatus(1L, DraftBatchStatusEnum.MATCHING);
        assertEquals(DraftBatchStatusEnum.MATCHING, matching.getStatus());
        assertTrue(matching.getProgress() >= 20);
        assertTrue(matching.getAllowedActions().contains("REFRESH_STATUS"));

        DraftBatchResponse matched = draftBatchService.changeStatus(1L, DraftBatchStatusEnum.MATCHED);
        assertEquals(DraftBatchStatusEnum.MATCHED, matched.getStatus());
        assertTrue(matched.getProgress() >= 60);
        assertTrue(matched.getAllowedActions().contains("CONFIRM"));
    }

    @Test
    void changeStatus_shouldRejectIllegalTransition() {
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> draftBatchService.changeStatus(1L, DraftBatchStatusEnum.IMPORTED)
        );

        assertTrue(ex.getMessage().contains("invalid status transition"));
    }

    @Test
    void changeStatus_shouldAllowRetryFromFailedToMatching() {
        batch.setStatus(DraftBatchStatusEnum.FAILED);
        batch.setProgress(0);
        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(batchRepository.save(any(UnifiedDraftBatch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DraftBatchResponse retried = draftBatchService.changeStatus(1L, DraftBatchStatusEnum.MATCHING);

        assertEquals(DraftBatchStatusEnum.MATCHING, retried.getStatus());
        assertTrue(retried.getProgress() >= 20);
        assertTrue(retried.getAllowedActions().contains("REFRESH_STATUS"));
    }
}
