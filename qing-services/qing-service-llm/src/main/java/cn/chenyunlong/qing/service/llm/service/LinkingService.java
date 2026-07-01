package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.service.llm.dto.link.LinkConfig;
import cn.chenyunlong.qing.service.llm.dto.link.LinkReport;
import cn.chenyunlong.qing.service.llm.dto.link.LinkReport.LinkDetail;
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
 * 跨渠道 TRACE → PRIMARY 关联服务。
 * <p>
 * 扫描账户下未关联的 TRACE 记录，按匹配规则找到对应的 PRIMARY 记录
 * 并设置 {@code targetPrimaryRecordId}。
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkingService {

    private final AccountRepository accountRepo;
    private final TransactionRecordRepository transactionRepo;
    private final SystemConfigService systemConfigService;

    /**
     * 对指定账户执行 TRACE → PRIMARY 自动关联。
     *
     * @param accountId 账户 ID
     * @param config    匹配规则配置
     * @return 关联结果报告
     */
    @Transactional
    public LinkReport link(Long accountId, LinkConfig config) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("账户不存在"));
        config = systemConfigService.fillDefaults(config);

        List<TransactionRecord> allRecords = transactionRepo.findAllByAccount(account);

        // 分别加载 PRIMARY 和未关联的 TRACE
        List<TransactionRecord> primaries = allRecords.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .filter(r -> r.getRecordRole() == RecordRoleEnum.PRIMARY)
                .toList();

        List<TransactionRecord> unlinkedTraces = new ArrayList<>(allRecords.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsDeleted()))
                .filter(r -> r.getRecordRole() == RecordRoleEnum.TRACE)
                .filter(r -> r.getTargetPrimaryRecordId() == null)
                .toList());

        LinkReport report = new LinkReport();
        report.setAccountId(accountId);
        report.setTotalTraceRecords(unlinkedTraces.size());

        if (unlinkedTraces.isEmpty() || primaries.isEmpty()) {
            report.setLinkedCount(0);
            report.setUnlinkedCount(unlinkedTraces.size());
            log.info("关联扫描 | accountId={} | 无待关联 TRACE 或无 PRIMARY 记录", accountId);
            return report;
        }

        int linked = 0;
        List<TransactionRecord> toUpdate = new ArrayList<>();
        List<LinkDetail> details = new ArrayList<>();

        for (TransactionRecord trace : unlinkedTraces) {
            TransactionRecord matchedPrimary = findMatchingPrimary(trace, primaries, config);
            if (matchedPrimary != null) {
                trace.setTargetPrimaryRecordId(matchedPrimary.getId());
                toUpdate.add(trace);
                linked++;

                LinkDetail detail = new LinkDetail();
                detail.setTraceRecordId(trace.getId());
                detail.setLinkedPrimaryRecordId(matchedPrimary.getId());
                detail.setMatchedBy(buildMatchedByFields(trace, matchedPrimary, config));
                details.add(detail);
            }
        }

        if (!toUpdate.isEmpty()) {
            transactionRepo.saveAll(toUpdate);
        }

        report.setLinkedCount(linked);
        report.setUnlinkedCount(unlinkedTraces.size() - linked);
        report.setDetails(details);

        log.info("关联完成 | accountId={} | 待关联={} | 成功={} | 未匹配={}",
                accountId, unlinkedTraces.size(), linked, unlinkedTraces.size() - linked);

        return report;
    }

    /**
     * 在 PRIMARY 列表中查找与 TRACE 记录匹配的 PRIMARY。
     */
    private TransactionRecord findMatchingPrimary(TransactionRecord trace,
                                                   List<TransactionRecord> primaries,
                                                   LinkConfig config) {
        for (TransactionRecord primary : primaries) {
            if (isMatch(trace, primary, config)) {
                return primary;
            }
        }
        return null;
    }

    /**
     * 判断 TRACE 与 PRIMARY 是否匹配（复用与 DedupService 相同的匹配逻辑）。
     */
    private boolean isMatch(TransactionRecord trace, TransactionRecord primary, LinkConfig config) {
        // 金额必须相等
        if (trace.getAmount().compareTo(primary.getAmount()) != 0) {
            return false;
        }
        // 方向必须一致
        if (trace.getDirectionType() != primary.getDirectionType()) {
            return false;
        }
        // 交易时间必须在容忍窗口内
        long diffMs = Math.abs(ChronoUnit.MILLIS.between(
                trace.getTransactionTime(), primary.getTransactionTime()));
        if (diffMs > TimeUnit.MINUTES.toMillis(config.getTimeToleranceMinutes())) {
            return false;
        }
        // 商户匹配（可选）
        if (config.getMatchMerchant()
                && !Objects.equals(trace.getMerchant(), primary.getMerchant())) {
            return false;
        }
        return true;
    }

    private List<String> buildMatchedByFields(TransactionRecord trace,
                                               TransactionRecord primary,
                                               LinkConfig config) {
        List<String> fields = new ArrayList<>(Arrays.asList("amount", "transactionTime", "directionType"));
        if (config.getMatchMerchant()
                && trace.getMerchant() != null && primary.getMerchant() != null) {
            fields.add("merchant");
        }
        return fields;
    }
}
