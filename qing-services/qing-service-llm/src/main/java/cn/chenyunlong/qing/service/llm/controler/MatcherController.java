package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionMatcher;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionMatcherRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
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
            return Result.error(404, "未找到指定的交易流水");
        }

        // 为了不影响数据库，可以克隆一个对象或者在一个事务中回滚。这里使用克隆或直接返回被修改的对象但不save
        // JPA实体如果是被Hibernate管理的，直接修改可能会触发Dirty Checking导致自动保存。
        // 所以我们最好不要将该实体保留在Session中，可以通过手动创建一个新的对象进行测试。
        TransactionRecord testRecord = cloneRecord(record);
        
        TestResult result = new TestResult();
        
        // 初始状态
        result.setOriginalRecord(cloneRecord(testRecord));

        // 应用规则
        try {
            matcherService.applyMatchers(testRecord, Collections.singletonList(req.getMatcher()));
            
            // 检查是否命中
            if (testRecord.getMatchRuleName() != null && testRecord.getMatchRuleName().equals(req.getMatcher().getName())) {
                result.setMatched(true);
            } else {
                result.setMatched(false);
            }
            
            result.setModifiedRecord(testRecord);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(500, "测试失败: " + e.getMessage());
        }
    }

    private TransactionRecord cloneRecord(TransactionRecord r) {
        TransactionRecord clone = new TransactionRecord();
        clone.setId(r.getId());
        clone.setTransactionTime(r.getTransactionTime());
        clone.setChannel(r.getChannel());
        clone.setAccount(r.getAccount());
        clone.setAccountName(r.getAccountName());
        clone.setAccountType(r.getAccountType());
        clone.setType(r.getType());
        clone.setAmount(r.getAmount());
        clone.setBalance(r.getBalance());
        clone.setCounterparty(r.getCounterparty());
        clone.setMerchant(r.getMerchant());
        clone.setCategory(r.getCategory());
        clone.setSubCategory(r.getSubCategory());
        clone.setStatus(r.getStatus());
        clone.setFee(r.getFee());
        clone.setOriginalId(r.getOriginalId());
        clone.setSourceFile(r.getSourceFile());
        clone.setRemark(r.getRemark());
        clone.setTags(r.getTags());
        clone.setFundType(r.getFundType());
        clone.setFundSource(r.getFundSource());
        clone.setFundSourceAccountId(r.getFundSourceAccountId());
        clone.setMatchStatus(cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum.ORIGINAL);
        return clone;
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
        private TransactionRecord modifiedRecord;
    }
}