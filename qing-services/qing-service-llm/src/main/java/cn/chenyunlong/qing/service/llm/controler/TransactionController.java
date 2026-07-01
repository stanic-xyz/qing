package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.dto.transactions.CreateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.dto.transactions.TransactionQueryDTO;
import cn.chenyunlong.qing.service.llm.dto.transactions.UpdateTransactionRecordDto;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.ReconciliationService;
import cn.chenyunlong.qing.service.llm.service.TransactionQueryService;
import cn.chenyunlong.qing.service.llm.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/finance/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRecordRepository transactionRepo;
    private final ReconciliationService reconciliationService;
    private final TransactionService transactionService;
    private final TransactionQueryService transactionQueryService;

    @GetMapping
    public Result<Page<TransactionRecord>> getTransactions(@ModelAttribute TransactionQueryDTO queryDTO) {
        Page<TransactionRecord> result = transactionQueryService.pageQuery(queryDTO);
        return Result.success(result);
    }

    /**
     * 新增单条流水。
     * 请求体需显式传入 `directionType`，且金额符号必须与方向保持一致。
     */
    @PostMapping
    public Result<TransactionRecord> createTransaction(@RequestBody @Valid CreateTransactionRecordDto dto) {
        TransactionService.CreateResult result = transactionService.create(dto);
        if (result.hasConflict()) {
            return new Result<>(200, "创建成功，" + result.getConflictMessage(), result.getRecord());
        }
        return Result.success(result.getRecord());
    }

    /**
     * 批量新增流水（支持同时创建多条）
     */
    @PostMapping("/batch")
    public Result<List<TransactionRecord>> createTransaction(@RequestBody @NotEmpty(message = "批量列表不能为空") List<@Valid CreateTransactionRecordDto> dtos) {
        List<TransactionRecord> transactionRecordList = transactionService.batchCreate(dtos);
        Result<List<TransactionRecord>> result = Result.success();
        result.setData(transactionRecordList);
        return result;
    }

    @GetMapping("/{id}/trace")
    public Result<List<TransactionRecord>> getTraceRecords(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        TransactionRecord record = getTransactionOrThrow(id);
        Page<TransactionRecord> related = transactionQueryService.findTraceRecords(record, page, size);
        return Result.success(related.getContent());
    }

    /**
     * 按显式字段更新单条流水。
     * `directionType` 未传保持原值，显式传值时按统一金额符号与方向契约校验。
     */
    @PutMapping("/{id}")
    public Result<TransactionRecord> updateTransaction(@PathVariable Long id,
                                                       @RequestBody @Valid UpdateTransactionRecordDto updateData) {
        TransactionRecord record = transactionService.update(id, updateData);
        return Result.success(record);
    }

    @GetMapping("/{id}")
    public Result<TransactionRecord> getTransaction(@PathVariable Long id) {
        TransactionRecord record = transactionRepo.getReferenceById(id);
        return Result.success(record);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTransaction(@PathVariable Long id) {
        TransactionRecord record = transactionRepo.findById(id).orElse(null);
        if (record != null) {
            record.setIsDeleted(true);
            transactionRepo.save(record);
        }
        return Result.success(null);
    }

    @PostMapping("/{id}/unmark-duplicate")
    public Result<Void> unmarkDuplicate(@PathVariable Long id) {
        TransactionRecord record = transactionRepo.findById(id).orElse(null);
        if (record != null) {
            record.setDuplicateOf(null);
            transactionRepo.save(record);
        }
        return Result.success(null);
    }

    @PostMapping("/reconciliation")
    public Result<Void> createReconciliation() {
        LocalDateTime now = LocalDateTime.now();
        reconciliationService.autoReconcile(now.minusYears(10), now.plusYears(10));
        return Result.success();
    }

    /**
     * 按流水 ID 加载流水，不存在时抛出资源不存在异常。
     *
     * @param transactionId 流水 ID
     * @return 流水实体
     */
    private TransactionRecord getTransactionOrThrow(Long transactionId) {
        return transactionRepo.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("记录不存在"));
    }
}
