package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.draft.CreateDraftBatchRequest;
import cn.chenyunlong.qing.service.llm.dto.draft.DraftBatchResponse;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftBatch;
import cn.chenyunlong.qing.service.llm.enums.DraftBatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DraftBatchService {

    private final UnifiedDraftBatchRepository batchRepository;

    /**
     * 创建草稿批次并返回标准视图。
     *
     * @param request 批次创建请求
     * @return 草稿批次响应
     */
    @Transactional
    public DraftBatchResponse create(CreateDraftBatchRequest request) {
        UnifiedDraftBatch batch = new UnifiedDraftBatch();
        batch.setAdapterType(request.getAdapterType());
        batch.setStatus(DraftBatchStatusEnum.DRAFTED);
        batch.setProgress(0);
        batch.setTotalRecords(request.getTotalRecords() == null ? 0 : request.getTotalRecords());
        batch.setBatchNo(genBatchNo());
        batch = batchRepository.save(batch);
        return toResponse(batch);
    }

    @Transactional(readOnly = true)
    public DraftBatchResponse get(Long id) {
        UnifiedDraftBatch batch = getBatchOrThrow(id);
        return toResponse(batch);
    }

    @Transactional
    public DraftBatchResponse changeStatus(Long id, DraftBatchStatusEnum targetStatus) {
        UnifiedDraftBatch batch = getBatchOrThrow(id);
        validateTransition(batch.getStatus(), targetStatus);
        batch.setStatus(targetStatus);
        if (targetStatus == DraftBatchStatusEnum.MATCHING) {
            batch.setProgress(Math.max(batch.getProgress(), 20));
        } else if (targetStatus == DraftBatchStatusEnum.MATCHED) {
            batch.setProgress(Math.max(batch.getProgress(), 60));
        } else if (targetStatus == DraftBatchStatusEnum.CONFIRMING) {
            batch.setProgress(Math.max(batch.getProgress(), 80));
        } else if (targetStatus == DraftBatchStatusEnum.IMPORTED) {
            batch.setProgress(100);
        }
        batch = batchRepository.save(batch);
        return toResponse(batch);
    }

    private static String genBatchNo() {
        int rand = ThreadLocalRandom.current().nextInt(1000, 10000);
        return "udb-" + LocalDate.now() + "-" + rand;
    }

    /**
     * 按 ID 加载批次，不存在时抛出资源不存在异常。
     *
     * @param id 批次 ID
     * @return 草稿批次实体
     */
    private UnifiedDraftBatch getBatchOrThrow(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("草稿批次不存在: " + id));
    }

    /**
     * 校验批次状态流转是否合法。
     *
     * @param current 当前状态
     * @param target 目标状态
     */
    private static void validateTransition(DraftBatchStatusEnum current, DraftBatchStatusEnum target) {
        if (target == null) {
            throw new IllegalArgumentException("targetStatus is required");
        }
        if (current == target) {
            return;
        }
        boolean ok = switch (current) {
            case DRAFTED -> target == DraftBatchStatusEnum.MATCHING || target == DraftBatchStatusEnum.FAILED;
            case MATCHING -> target == DraftBatchStatusEnum.MATCHED || target == DraftBatchStatusEnum.FAILED;
            case MATCHED -> target == DraftBatchStatusEnum.CONFIRMING || target == DraftBatchStatusEnum.FAILED;
            case CONFIRMING -> target == DraftBatchStatusEnum.IMPORTED || target == DraftBatchStatusEnum.FAILED;
            case IMPORTED -> false;
            case FAILED -> target == DraftBatchStatusEnum.MATCHING;
        };
        if (!ok) {
            throw new BusinessException("invalid status transition: " + current + " -> " + target);
        }
    }

    private static DraftBatchResponse toResponse(UnifiedDraftBatch batch) {
        return DraftBatchResponse.builder()
                .id(batch.getId())
                .batchNo(batch.getBatchNo())
                .adapterType(batch.getAdapterType())
                .status(batch.getStatus())
                .progress(batch.getProgress())
                .totalRecords(batch.getTotalRecords())
                .errorMessage(batch.getErrorMessage())
                .createdAt(batch.getCreatedAt())
                .updatedAt(batch.getUpdatedAt())
                .allowedActions(resolveActions(batch.getStatus()))
                .build();
    }

    private static List<String> resolveActions(DraftBatchStatusEnum status) {
        List<String> actions = new ArrayList<>();
        switch (status) {
            case DRAFTED -> actions.add("START_MATCH");
            case MATCHING -> actions.add("REFRESH_STATUS");
            case MATCHED -> actions.add("CONFIRM");
            case CONFIRMING -> actions.add("IMPORT");
            case IMPORTED -> actions.add("VIEW_RESULT");
            case FAILED -> actions.add("RETRY");
        }
        return actions;
    }
}
