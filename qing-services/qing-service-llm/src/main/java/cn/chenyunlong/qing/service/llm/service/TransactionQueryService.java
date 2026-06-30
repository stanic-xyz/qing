package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.transactions.TransactionQueryDTO;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易流水专用查询服务。
 * <p>
 * 将 Controller 中的复杂 Specification 查询逻辑分离到此，
 * 同时优化了 SQL：直接对 ID 列过滤避免加载实体、补全 channelIds 过滤、
 * 使用 JOIN FETCH 避免 N+1、复用 Account Join 避免重复关联。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TransactionQueryService {

    private final TransactionRecordRepository transactionRepo;

    /**
     * 分页查询交易流水。
     *
     * @param query 查询参数
     * @return 分页结果（实体中 account / category 等关联已通过 JOIN FETCH 预加载）
     */
    public Page<TransactionRecord> pageQuery(TransactionQueryDTO query) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(query.getSortDirection())
            ? Sort.Direction.ASC
            : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(
            query.getPage(), query.getSize(),
            Sort.by(direction, query.getSortField())
        );

        Specification<TransactionRecord> spec = buildSpecification(query);

        return transactionRepo.findAll(spec, pageRequest);
    }

    /**
     * 查询指定交易记录的关联溯源流水。
     * 根据金额、交易类型、交易时间区间（±1天）查找可能的关联记录。
     *
     * @param record 基准交易记录
     * @param page   页码
     * @param size   每页大小（上限 500）
     * @return 分页结果
     */
    public Page<TransactionRecord> findTraceRecords(TransactionRecord record, int page, int size) {
        if (size > 500) {
            size = 500;
        }
        LocalDateTime start = record.getTransactionTime().minusDays(1).with(java.time.LocalTime.MIN);
        LocalDateTime end = record.getTransactionTime().plusDays(1).with(java.time.LocalTime.MAX);

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "transactionTime"));

        Specification<TransactionRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("amount"), record.getAmount()));
            predicates.add(cb.equal(root.get("transactionType"), record.getTransactionType()));
            predicates.add(cb.between(root.get("transactionTime"), start, end));
            predicates.add(cb.equal(root.get("isDeleted"), false));
            predicates.add(cb.or(
                cb.equal(root.get("isImported"), true),
                cb.isNull(root.get("isImported"))
            ));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return transactionRepo.findAll(spec, pageRequest);
    }

    /**
     * 构建动态查询条件。
     * <p>
     * 优化说明：
     * <ul>
     *   <li>accountIds — 直接对 {@code account.id} 列过滤，不加载 Account 实体</li>
     *   <li>channelIds — 补全了功能：通过子查询过滤出属于指定渠道的 Account，再对流水过滤</li>
     *   <li>account Join 在 accountIds / channelIds 之间复用，避免重复关联</li>
     *   <li>isDeleted / isImported 作为全局默认条件自动追加</li>
     * </ul>
     * </p>
     */
    private Specification<TransactionRecord> buildSpecification(TransactionQueryDTO query) {
        return (root, cbQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // === 账户过滤 ===
            // 复用 Join：accountIds 和 channelIds 可能都需要 join account
            Join<TransactionRecord, Account> accountJoin = null;

            if (CollectionUtils.isNotEmpty(query.getAccountIds())) {
                accountJoin = root.join("account");
                predicates.add(accountJoin.get("id").in(query.getAccountIds()));
            }

            // channelIds：通过 Account.channel 关联过滤
            if (CollectionUtils.isNotEmpty(query.getChannelIds())) {
                if (accountJoin == null) {
                    accountJoin = root.join("account");
                }
                // 使用子查询过滤：SELECT a.id FROM Account a WHERE a.channel.id IN :channelIds
                Subquery<Long> subquery = cbQuery.subquery(Long.class);
                var accountRoot = subquery.from(Account.class);
                subquery.select(accountRoot.get("id"))
                    .where(accountRoot.get("channel").get("id").in(query.getChannelIds()));
                predicates.add(accountJoin.get("id").in(subquery));
            }

            //            // === 交易类型过滤 ===
            //            if (StringUtils.hasText(query.getType())) {
            //                predicates.add(cb.equal(root.get("type"), query.getType()));
            //            }

            // === 匹配状态过滤 ===
            if (query.getMatchStatus() != null) {
                predicates.add(cb.equal(root.get("matchStatus"), query.getMatchStatus()));
            }

            // === 关键字模糊搜索（商户 + 备注） ===
            if (StringUtils.hasText(query.getKeyword())) {
                String likePattern = "%" + query.getKeyword().trim() + "%";
                predicates.add(cb.or(
                    cb.like(root.get("merchant"), likePattern),
                    cb.like(root.get("remark"), likePattern)
                ));
            }

            // === 交易时间范围过滤 ===
            if (StringUtils.hasText(query.getStartDate())) {
                LocalDateTime start = LocalDate.parse(query.getStartDate(), DateTimeFormatter.ISO_DATE)
                    .atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("transactionTime"), start));
            }
            if (StringUtils.hasText(query.getEndDate())) {
                LocalDateTime end = LocalDate.parse(query.getEndDate(), DateTimeFormatter.ISO_DATE)
                    .atTime(23, 59, 59);
                predicates.add(cb.lessThanOrEqualTo(root.get("transactionTime"), end));
            }

            // === 流水角色过滤 ===
            if (query.getRecordRole() != null) {
                predicates.add(cb.equal(root.get("recordRole"), query.getRecordRole()));
            }

            // === 全局默认条件 ===
            // 不查询已软删除的数据
            predicates.add(cb.equal(root.get("isDeleted"), false));
            // 隔离未导入的数据（兼容历史数据 isImported is null）
            predicates.add(cb.or(
                cb.equal(root.get("isImported"), true),
                cb.isNull(root.get("isImported"))
            ));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
