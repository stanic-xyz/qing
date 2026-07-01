package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.dedup.DedupConfig;
import cn.chenyunlong.qing.service.llm.dto.dedup.DedupReport;
import cn.chenyunlong.qing.service.llm.dto.dedup.DedupReport.DedupGroupDetail;
import cn.chenyunlong.qing.service.llm.dto.dedup.CleanupRequest;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.RecordRoleEnum;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * PRIMARY 记录去重服务。
 * <p>
 * 按可配置规则扫描账户下所有 PRIMARY 记录，找出重复记录并用 {@code duplicateOf} 标记。
 * TRACE 和手工记录不参与此扫描，TRACE 的去重在导入时按渠道严格匹配。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DedupService {

    private final AccountRepository accountRepo;
    private final TransactionRecordRepository transactionRepo;
    private final SystemConfigService systemConfigService;

    /**
     * 对指定账户执行全量扫描去重。
     *
     * @param accountId 账户 ID
     * @param config    匹配规则配置
     * @return 去重报告
     */
    @Transactional
    public DedupReport deduplicate(Long accountId, DedupConfig config) {
        Account account = accountRepo.findById(accountId)
            .orElseThrow(() -> new NotFoundException("账户不存在"));
        config = systemConfigService.fillDefaults(config);

        List<TransactionRecord> candidates = transactionRepo.findAllByAccount(account).stream()
            .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
            .filter(r -> r.getDuplicateOf() == null)
            .filter(r -> r.getRecordRole() == RecordRoleEnum.PRIMARY)
            .toList();

        return scanDuplicates(accountId, candidates, config, true);
    }

    /**
     * 清理预览：扫描账户下 PRIMARY 重复记录并返回报告，但不做任何标记。
     * 用户根据报告确认后，再调用 {@link #executeCleanup} 执行软删除。
     */
    public DedupReport previewCleanup(Long accountId, CleanupRequest request) {
        Account account = accountRepo.findById(accountId)
            .orElseThrow(() -> new NotFoundException("账户不存在"));
        if (request == null) {
            request = new CleanupRequest();
        }

        List<TransactionRecord> candidates = transactionRepo.findAllByAccount(account).stream()
            .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
            .filter(r -> r.getDuplicateOf() == null)
            .filter(r -> r.getRecordRole() == RecordRoleEnum.PRIMARY)
            .toList();

        DedupConfig config = new DedupConfig();
        config.setTimeToleranceMinutes(request.getTimeToleranceMinutes());
        config.setMatchMerchant(request.isMatchMerchant());

        return scanDuplicates(accountId, candidates, config, false);
    }

    /**
     * 执行清理：对指定的重复记录执行软删除（{@code isDeleted = true}）。
     *
     * @param accountId 账户 ID
     * @param request   清理请求（需包含 {@code duplicateRecordIds}）
     * @return 实际删除的记录数
     */
    @Transactional
    public int executeCleanup(Long accountId, CleanupRequest request) {
        Account account = accountRepo.findById(accountId)
            .orElseThrow(() -> new NotFoundException("账户不存在"));

        List<TransactionRecord> toDelete = transactionRepo.findAllByAccount(account).stream()
            .filter(r -> request.getDuplicateRecordIds().contains(r.getId()))
            .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
            .toList();

        for (TransactionRecord record : toDelete) {
            record.setIsDeleted(true);
        }
        transactionRepo.saveAll(toDelete);

        log.info("清理完成 | accountId={} | 删除={}", accountId, toDelete.size());
        return toDelete.size();
    }

    /**
     * 核心扫描逻辑：对候选 PRIMARY 记录分组去重，可选是否标记 {@code duplicateOf}。
     *
     * @param accountId  账户 ID
     * @param candidates 候选记录列表（已按角色过滤）
     * @param config     匹配规则
     * @param mark       是否标记 duplicateOf（true=标记并持久化，false=仅返回报告）
     */
    private DedupReport scanDuplicates(Long accountId, List<TransactionRecord> candidates,
                                        DedupConfig config, boolean mark) {
        DedupReport report = new DedupReport();
        report.setAccountId(accountId);
        report.setTotalRecords(candidates.size());

        if (candidates.size() < 2) {
            log.info("去重扫描 | accountId={} | 记录不足 2 条，无需去重", accountId);
            return report;
        }

        List<TransactionRecord> sorted = new ArrayList<>(candidates);
        sorted.sort(Comparator.comparing(TransactionRecord::getTransactionTime)
            .thenComparing(TransactionRecord::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(TransactionRecord::getId));

        List<TransactionRecord> unprocessed = new ArrayList<>(sorted);
        List<List<TransactionRecord>> duplicateGroups = new ArrayList<>();
        List<TransactionRecord> toMark = new ArrayList<>();

        while (!unprocessed.isEmpty()) {
            TransactionRecord first = unprocessed.remove(0);
            List<TransactionRecord> group = new ArrayList<>();
            group.add(first);

            Iterator<TransactionRecord> it = unprocessed.iterator();
            while (it.hasNext()) {
                TransactionRecord candidate = it.next();
                if (isDuplicate(first, candidate, config)) {
                    group.add(candidate);
                    it.remove();
                }
            }

            if (group.size() > 1) {
                duplicateGroups.add(group);
                if (mark) {
                    TransactionRecord kept = group.getFirst();
                    for (int i = 1; i < group.size(); i++) {
                        TransactionRecord dup = group.get(i);
                        dup.setDuplicateOf(kept.getId());
                        toMark.add(dup);
                    }
                }
            }
        }

        if (!toMark.isEmpty()) {
            transactionRepo.saveAll(toMark);
        }

        report.setDuplicateGroups(duplicateGroups.size());
        report.setMarkedCount(toMark.size());
        report.setKeptCount(duplicateGroups.size());

        for (List<TransactionRecord> group : duplicateGroups) {
            TransactionRecord kept = group.getFirst();
            DedupGroupDetail detail = new DedupGroupDetail();
            detail.setKeptRecordId(kept.getId());

            List<String> matchedBy = buildMatchedByFields(kept, group.get(1), config);
            detail.setMatchedBy(matchedBy);

            List<Long> dupIds = new ArrayList<>();
            for (int i = 1; i < group.size(); i++) {
                if (dupIds.size() >= 99) {
                    detail.setHasMore(true);
                    break;
                }
                dupIds.add(group.get(i).getId());
            }
            detail.setDuplicateRecordIds(dupIds);
            report.getDetails().add(detail);
        }

        log.info("去重扫描完成 | accountId={} | 扫描={} | 重复组={} | 标记={}",
            accountId, candidates.size(), duplicateGroups.size(), toMark.size());

        return report;
    }

    /**
     * 检测一条待创建的 PRIMARY 记录是否与账户下现有 PRIMARY 记录重复。
     * TRACE 和手工记录不参与此检查。
     *
     * @param account   所属账户
     * @param candidate 待检查的记录
     * @param config    匹配规则
     * @return 匹配到的第一条现有 PRIMARY 记录，无匹配返回 null
     */
    public TransactionRecord findDuplicate(Account account, TransactionRecord candidate, DedupConfig config) {
        config = systemConfigService.fillDefaults(config);
        List<TransactionRecord> existing = transactionRepo.findAllByAccount(account);
        for (TransactionRecord record : existing) {
            if (Boolean.TRUE.equals(record.getIsDeleted())
                || record.getDuplicateOf() != null
                || record.getRecordRole() != RecordRoleEnum.PRIMARY) {
                continue;
            }
            if (isDuplicate(candidate, record, config)) {
                return record;
            }
        }
        return null;
    }

    /**
     * 判断两条记录是否重复。
     */
    private boolean isDuplicate(TransactionRecord a, TransactionRecord b, DedupConfig config) {
        // 金额必须相等
        if (a.getAmount().compareTo(b.getAmount()) != 0) {
            return false;
        }

        // 方向必须一致
        if (a.getDirectionType() != b.getDirectionType()) {
            return false;
        }

        // 交易时间必须在容忍窗口内
        long diffMs = Math.abs(ChronoUnit.MILLIS.between(a.getTransactionTime(), b.getTransactionTime()));
        if (diffMs > TimeUnit.MINUTES.toMillis(config.getTimeToleranceMinutes())) {
            return false;
        }

        // 商户匹配（可选）
        if (config.getMatchMerchant() && !Objects.equals(a.getMerchant(), b.getMerchant())) {
            return false;
        }

        // 详情匹配（可选）
        if (config.getMatchDetail() && !Objects.equals(a.getDetail(), b.getDetail())) {
            return false;
        }

        return true;
    }

    /**
     * 根据配置和实际值构建命中的匹配字段列表。
     */
    private List<String> buildMatchedByFields(TransactionRecord a, TransactionRecord b, DedupConfig config) {
        List<String> fields = new ArrayList<>(Arrays.asList("amount", "transactionTime", "directionType"));

        if (config.getMatchMerchant() && a.getMerchant() != null && b.getMerchant() != null) {
            fields.add("merchant");
        }
        if (config.getMatchDetail() && a.getDetail() != null && b.getDetail() != null) {
            fields.add("detail");
        }
        return fields;
    }
}
