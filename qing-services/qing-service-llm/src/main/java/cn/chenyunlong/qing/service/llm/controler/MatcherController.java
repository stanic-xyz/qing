package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.entity.UnifiedDraftRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.repository.UnifiedDraftRecordRepository;
import cn.chenyunlong.qing.service.llm.service.MatcherService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/finance/matchers")
@RequiredArgsConstructor
public class MatcherController {

    private final TransactionMatcherRepository matcherRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final UnifiedDraftRecordRepository unifiedDraftRecordRepository;
    private final MatcherService matcherService;

    @GetMapping
    public Result<List<TransactionMatcher>> getAll() {
        return Result.success(matcherRepository.findAll());
    }

    @GetMapping("/active")
    public Result<List<TransactionMatcher>> getActive() {
        return Result.success(matcherRepository.findByIsActiveTrueOrderByPriorityDesc());
    }

    @PostMapping
    public Result<TransactionMatcher> save(@RequestBody TransactionMatcher matcher) {
        return Result.success(matcherRepository.save(matcher));
    }

    @PutMapping("/{id}")
    public Result<TransactionMatcher> update(@PathVariable("id") Long id, @RequestBody TransactionMatcher matcher) {
        matcher.setId(id);
        return Result.success(matcherRepository.save(matcher));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        matcherRepository.deleteById(id);
        return Result.success(null);
    }

    @PostMapping("/test")
    public Result<TestResult> testMatcher(@RequestBody TestRequest req) {
        TransactionRecord record = transactionRecordRepository.findById(req.getTransactionId()).orElse(null);
        if (record == null) {
            UnifiedDraftRecord unifiedDraftRecord = unifiedDraftRecordRepository.findById(req.getTransactionId()).orElse(null);
            if (unifiedDraftRecord == null) {
                throw new NotFoundException("未找到指定的交易流水");
            }
            return executeTest(req, unifiedDraftRecord, null);
        }
        return executeTest(req, toUnifiedDraftRecord(record), record);
    }

    /**
     * 执行规则测试并返回测试结果。
     */
    private Result<TestResult> executeTest(TestRequest req, UnifiedDraftRecord testRecord, TransactionRecord originalRecord) {
        TestResult result = new TestResult();
        result.setOriginalRecord(originalRecord == null ? null : cloneRecord(originalRecord));

        // 应用规则
        matcherService.applyMatchers(testRecord, Collections.singletonList(req.getMatcher()));

        // 草稿模型不记录 matchRuleName，以状态判断是否命中。
        result.setMatched(testRecord.getMatchStatus() != null && testRecord.getMatchStatus().name().contains("MATCH"));

        result.setModifiedRecord(testRecord);
        return Result.success(result);
    }

    private TransactionRecord cloneRecord(TransactionRecord r) {
        TransactionRecord clone = new TransactionRecord();
        clone.setId(r.getId());
        clone.setTransactionTime(r.getTransactionTime());
        clone.setAccount(r.getAccount());
        clone.setAccountName(r.getAccountName());
        clone.setAccountType(r.getAccountType());
        clone.setTransactionType(r.getTransactionType());
        clone.setAmount(r.getAmount());
        clone.setBalance(r.getBalance());
        clone.setCounterparty(r.getCounterparty());
        clone.setMerchant(r.getMerchant());
        clone.setCategory(r.getCategory());
        clone.setSubCategory(r.getSubCategory());
        clone.setStatus(r.getStatus());
        clone.setFee(r.getFee());
        clone.setDetail(r.getDetail());
        clone.setTags(r.getTags());
        clone.setFundSource(r.getFundSource());
        clone.setFundSourceAccountId(r.getFundSourceAccountId());
        clone.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.ORIGINAL);
        return clone;
    }

    /**
     * 将历史交易记录映射为可测试的统一草稿记录。
     */
    private UnifiedDraftRecord toUnifiedDraftRecord(TransactionRecord r) {
        UnifiedDraftRecord draft = new UnifiedDraftRecord();
        draft.setId(r.getId());
        draft.setTransactionTime(r.getTransactionTime());
        draft.setAmount(r.getAmount());
        draft.setMerchant(r.getMerchant());
        draft.setCategory(r.getCategory());
        draft.setCounterparty(r.getCounterparty());
        draft.setTrasactionType(r.getTransactionType());
        return draft;
    }

    @Data
    public static class TestRequest {
        private Long transactionId;
        private TransactionMatcher matcher;
    }

    @Data
    public static class TestResult {
        private boolean matched;
        private TransactionRecord originalRecord;
        private UnifiedDraftRecord modifiedRecord;
    }
}
