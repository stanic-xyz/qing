package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.MatchStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import cn.chenyunlong.qing.service.llm.service.ReconciliationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.repository.ChannelRepository;

@RestController
@RequestMapping("/api/finance/transactions")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRecordRepository transactionRepo;
    private final ChannelRepository channelRepo;
    private final AccountRepository accountRepository;
    private final ReconciliationService reconciliationService;

    @GetMapping
    public Result<Page<TransactionRecord>> getTransactions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "channelIds", required = false) List<Long> channelIds,
            @RequestParam(value = "accountIds", required = false) List<Long> accountIds,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "matchStatus", required = false) MatchStatusEnum matchStatus,
            @RequestParam(value = "sortField", defaultValue = "transactionTime") String sortField,
            @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortField));

        Specification<TransactionRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (channelIds != null && !channelIds.isEmpty()) {
                List<Channel> channels = channelRepo.findAllById(channelIds);
                if (!channels.isEmpty()) {
                    predicates.add(root.get("channel").in(channels));
                } else {
                    predicates.add(cb.disjunction()); // Return empty if no codes match
                }
            }
            if (CollectionUtils.isNotEmpty(accountIds)) {
                List<Account> accounts = accountRepository.findAllById(accountIds);
                if (!accounts.isEmpty()) {
                    predicates.add(root.get("account").in(accounts));
                }
            }
            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (matchStatus != null) {
                predicates.add(cb.equal(root.get("matchStatus"), matchStatus));
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim() + "%";
                predicates.add(cb.or(
                        cb.like(root.get("counterparty"), likePattern),
                        cb.like(root.get("merchant"), likePattern),
                        cb.like(root.get("remark"), likePattern)
                ));
            }
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("transactionTime"), start));
            }
            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("transactionTime"), end));
            }

            // 默认不查询已软删除的数据
            predicates.add(cb.equal(root.get("isDeleted"), false));

            // 彻底隔离未导入的数据 (兼容历史数据 isImported is null)
            predicates.add(cb.or(
                    cb.equal(root.get("isImported"), true),
                    cb.isNull(root.get("isImported"))
            ));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<TransactionRecord> result = transactionRepo.findAll(spec, pageRequest);
        return Result.success(result);
    }

    @PutMapping("/{id}")
    public Result<TransactionRecord> updateTransaction(@PathVariable Long id, @RequestBody TransactionRecord updateData) {
        TransactionRecord record = transactionRepo.findById(id).orElse(null);
        if (record == null) {
            return Result.error(404, "记录不存在");
        }

        // 允许修改某些字段
        if (updateData.getCategory() != null) record.setCategory(updateData.getCategory());
        if (updateData.getAmount() != null) record.setAmount(updateData.getAmount());
        if (updateData.getCounterparty() != null) record.setCounterparty(updateData.getCounterparty());
        if (updateData.getMerchant() != null) record.setMerchant(updateData.getMerchant());
        if (updateData.getType() != null) record.setType(updateData.getType());

        record.setIsModified(true);
        transactionRepo.save(record);

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

    @PostMapping("/reconciliation")
    public Result<Void> createReconciliation() {
        LocalDateTime now = LocalDateTime.now();
        reconciliationService.autoReconcile(now.minusYears(10), now.plusYears(10));
        return Result.success();
    }
}
