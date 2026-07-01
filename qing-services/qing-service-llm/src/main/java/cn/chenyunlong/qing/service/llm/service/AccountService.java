package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.account.AccountDTO;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.ReconciliationStatusEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 账户管理服务。
 * <p>
 * 仅依赖 Repository 层，不依赖任何其他 Service，避免服务间耦合。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final TransactionRecordRepository transactionRepo;

    /**
     * 根据账户下所有历史交易流水重新计算并更新账户余额。
     * <p>
     * 计算规则：
     * <ul>
     *   <li>起始值 = {@code initialBalance}（期初余额）</li>
     *   <li>INCOME 类型的流水 → 增加余额</li>
     *   <li>EXPENSE 类型的流水 → 减少余额</li>
     *   <li>已软删除的流水不纳入计算</li>
     * </ul>
     * </p>
     *
     * @param accountId 账户 ID
     * @return 更新后的账户 DTO（包含新的 currentBalance / balanceAsOf）
     */
    @Transactional
    public AccountDTO calculateBalance(Long accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("账户不存在"));

        List<TransactionRecord> records = transactionRepo.findAllByAccount(account);

        BigDecimal balance = account.getInitialBalance() != null
                ? account.getInitialBalance()
                : BigDecimal.ZERO;

        for (TransactionRecord record : records) {
            if (Boolean.TRUE.equals(record.getIsDeleted()) || record.getAmount() == null) {
                continue;
            }
            switch (record.getDirectionType()) {
                case INCOME -> balance = balance.add(record.getAmount());
                case EXPENSE -> balance = balance.subtract(record.getAmount());
            }
        }

        account.setCurrentBalance(balance);
        account.setBalanceAsOf(LocalDateTime.now());
        accountRepo.save(account);

        return AccountDTO.of(account);
    }

    /**
     * 对指定账户执行逐笔对账。
     * <p>
     * 按交易时间升序遍历账户下所有流水，维护实时余额（running balance），
     * 将每条流水的 {@code balance} 字段与实时余额比对：
     * <ul>
     *   <li>一致 → 更新 {@code reconciliationStatus = MATCHED}</li>
     *   <li>不一致 → 更新 {@code reconciliationStatus = MANUAL}（需人工核实）</li>
     *   <li>无余额信息 → 跳过，保留原有对账状态</li>
     * </ul>
     * 同时将账户的 {@code currentBalance} 同步为最终的实时余额。
     * </p>
     *
     * @param accountId 账户 ID
     * @return 对账并更新后的账户 DTO
     */
    @Transactional
    public AccountDTO reconciliation(Long accountId) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("账户不存在"));

        List<TransactionRecord> records = transactionRepo.findAllByAccount(account);
        // 按交易时间升序排列，时间相同时按 ID 排序以确保确定性
        records.sort(Comparator.comparing(TransactionRecord::getTransactionTime)
                .thenComparing(TransactionRecord::getId));

        BigDecimal runningBalance = account.getInitialBalance() != null
                ? account.getInitialBalance()
                : BigDecimal.ZERO;

        int checked = 0;
        int matched = 0;
        int mismatched = 0;
        int skipped = 0;

        for (TransactionRecord record : records) {
            if (Boolean.TRUE.equals(record.getIsDeleted()) || record.getAmount() == null) {
                continue;
            }

            // 应用交易影响：INCOME 增加余额，EXPENSE 减少余额
            switch (record.getDirectionType()) {
                case INCOME -> runningBalance = runningBalance.add(record.getAmount());
                case EXPENSE -> runningBalance = runningBalance.subtract(record.getAmount());
            }

            // 有余额字段才参与对账校验
            if (record.getBalance() != null) {
                checked++;
                if (runningBalance.compareTo(record.getBalance()) == 0) {
                    record.setReconciliationStatus(ReconciliationStatusEnum.MATCHED);
                    matched++;
                } else {
                    record.setReconciliationStatus(ReconciliationStatusEnum.MANUAL);
                    mismatched++;
                    log.warn("对账不一致 | accountId={} | recordId={} | 预期余额={} | 账面余额={}",
                            accountId, record.getId(), runningBalance, record.getBalance());
                }
            } else {
                skipped++;
            }
        }

        transactionRepo.saveAll(records);

        // 同步更新账户当前余额
        account.setCurrentBalance(runningBalance);
        account.setBalanceAsOf(LocalDateTime.now());
        accountRepo.save(account);

        log.info("对账完成 | accountId={} | 总流水={} | 已检查={} | 一致={} | 不一致={} | 跳过={}",
                accountId, records.size(), checked, matched, mismatched, skipped);

        return AccountDTO.of(account);
    }
}
