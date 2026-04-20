package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardService {

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * 月度收支汇总 (按月统计收入/支出/笔数)
     */
    public MonthlyOverview getMonthlyOverview(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                .filter(r -> r.getIsImported() != null && r.getIsImported())
                .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                .filter(r -> r.getTransactionTime() != null)
                .filter(r -> {
                    String ts = r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    return ts.equals(prefix);
                })
                .collect(Collectors.toList());

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        int incomeCount = 0;
        int expenseCount = 0;

        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            if (r.getType() != null && "INCOME".equals(r.getType().name())) {
                income = income.add(r.getAmount());
                incomeCount++;
            } else if (r.getType() != null && "EXPENSE".equals(r.getType().name())) {
                expense = expense.add(r.getAmount());
                expenseCount++;
            }
        }

        MonthlyOverview dto = new MonthlyOverview();
        dto.setYear(year);
        dto.setMonth(month);
        dto.setIncome(income.setScale(2, RoundingMode.HALF_UP).toString());
        dto.setExpense(expense.setScale(2, RoundingMode.HALF_UP).toString());
        dto.setNetIncome(income.subtract(expense).setScale(2, RoundingMode.HALF_UP).toString());
        dto.setIncomeCount(incomeCount);
        dto.setExpenseCount(expenseCount);
        return dto;
    }

    /**
     * 年度趋势 (近12个月的收入/支出/净现金流)
     */
    public List<MonthlyTrend> getYearlyTrend(int year) {
        YearMonth start = YearMonth.of(year, 1);
        List<MonthlyTrend> trends = new ArrayList<>();

        for (int m = 1; m <= 12; m++) {
            YearMonth ym = YearMonth.of(year, m);
            String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                    .filter(r -> r.getIsImported() != null && r.getIsImported())
                    .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                    .filter(r -> r.getTransactionTime() != null)
                    .filter(r -> {
                        String ts = r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        return ts.equals(prefix);
                    })
                    .collect(Collectors.toList());

            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;

            for (TransactionRecord r : records) {
                if (r.getAmount() == null) continue;
                if (r.getType() != null && "INCOME".equals(r.getType().name())) {
                    income = income.add(r.getAmount());
                } else if (r.getType() != null && "EXPENSE".equals(r.getType().name())) {
                    expense = expense.add(r.getAmount());
                }
            }

            MonthlyTrend trend = new MonthlyTrend();
            trend.setYear(year);
            trend.setMonth(m);
            trend.setIncome(income.setScale(2, RoundingMode.HALF_UP).toString());
            trend.setExpense(expense.setScale(2, RoundingMode.HALF_UP).toString());
            trend.setNetCashflow(income.subtract(expense).setScale(2, RoundingMode.HALF_UP).toString());
            trends.add(trend);
        }

        return trends;
    }

    /**
     * 分类支出排行 (指定月份各分类支出金额)
     */
    public List<CategorySpending> getCategorySpending(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                .filter(r -> r.getIsImported() != null && r.getIsImported())
                .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                .filter(r -> r.getTransactionTime() != null)
                .filter(r -> "EXPENSE".equals(r.getType() != null ? r.getType().name() : null))
                .filter(r -> {
                    String ts = r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    return ts.equals(prefix);
                })
                .collect(Collectors.toList());

        Map<String, BigDecimal> categoryMap = new LinkedHashMap<>();
        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            String catName = (r.getCategory() != null && r.getCategory().getName() != null)
                    ? r.getCategory().getName() : "未分类";
            categoryMap.put(catName, categoryMap.getOrDefault(catName, BigDecimal.ZERO).add(r.getAmount()));
        }

        return categoryMap.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(e -> {
                    CategorySpending cs = new CategorySpending();
                    cs.setCategory(e.getKey());
                    cs.setAmount(e.getValue().setScale(2, RoundingMode.HALF_UP).toString());
                    return cs;
                })
                .collect(Collectors.toList());
    }

    /**
     * 支付方式统计 (各渠道/账户消费占比)
     */
    public List<PaymentMethodStats> getPaymentMethodStats(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                .filter(r -> r.getIsImported() != null && r.getIsImported())
                .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                .filter(r -> r.getTransactionTime() != null)
                .filter(r -> {
                    String ts = r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    return ts.equals(prefix);
                })
                .collect(Collectors.toList());

        BigDecimal total = records.stream()
                .filter(r -> r.getAmount() != null)
                .map(TransactionRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> methodMap = new LinkedHashMap<>();
        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            String method;
            if (r.getChannel() != null && r.getChannel().getChannelName() != null) {
                method = r.getChannel().getChannelName();
            } else if (r.getAccount() != null && r.getAccount().getAccountName() != null) {
                method = r.getAccount().getAccountName();
            } else {
                method = "其他";
            }
            methodMap.put(method, methodMap.getOrDefault(method, BigDecimal.ZERO).add(r.getAmount()));
        }

        return methodMap.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(e -> {
                    PaymentMethodStats stats = new PaymentMethodStats();
                    stats.setPaymentMethod(e.getKey());
                    stats.setAmount(e.getValue().setScale(2, RoundingMode.HALF_UP).toString());
                    BigDecimal percent = total.compareTo(BigDecimal.ZERO) > 0
                            ? e.getValue().divide(total, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;
                    stats.setPercent(percent.setScale(2, RoundingMode.HALF_UP).toString());
                    return stats;
                })
                .collect(Collectors.toList());
    }

    /**
     * 收支趋势 (近N个月)
     */
    public List<MonthlyTrend> getTrend(int months) {
        YearMonth current = YearMonth.now();
        List<MonthlyTrend> trends = new ArrayList<>();

        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = current.minusMonths(i);
            String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));

            List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                    .filter(r -> r.getIsImported() != null && r.getIsImported())
                    .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                    .filter(r -> r.getTransactionTime() != null)
                    .filter(r -> {
                        String ts = r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                        return ts.equals(prefix);
                    })
                    .collect(Collectors.toList());

            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;

            for (TransactionRecord r : records) {
                if (r.getAmount() == null) continue;
                if (r.getType() != null && "INCOME".equals(r.getType().name())) {
                    income = income.add(r.getAmount());
                } else if (r.getType() != null && "EXPENSE".equals(r.getType().name())) {
                    expense = expense.add(r.getAmount());
                }
            }

            MonthlyTrend trend = new MonthlyTrend();
            trend.setYear(ym.getYear());
            trend.setMonth(ym.getMonthValue());
            trend.setIncome(income.setScale(2, RoundingMode.HALF_UP).toString());
            trend.setExpense(expense.setScale(2, RoundingMode.HALF_UP).toString());
            trend.setNetCashflow(income.subtract(expense).setScale(2, RoundingMode.HALF_UP).toString());
            trends.add(trend);
        }

        return trends;
    }

    /**
     * 账户余额概览 (所有账户当前余额)
     */
    public List<AccountBalance> getAccountBalances() {
        List<Account> accounts = accountRepository.findAll().stream()
                .filter(a -> a.getStatus() != null && "ACTIVE".equals(a.getStatus().name()))
                .collect(Collectors.toList());

        return accounts.stream().map(a -> {
            AccountBalance ab = new AccountBalance();
            ab.setAccountId(a.getId() != null ? a.getId().toString() : null);
            ab.setAccountName(a.getAccountName());
            ab.setAccountType(a.getAccountType() != null ? a.getAccountType().name() : null);
            ab.setBankName(a.getBankName());
            ab.setBalance(a.getCurrentBalance() != null
                    ? a.getCurrentBalance().setScale(2, RoundingMode.HALF_UP).toString()
                    : "0.00");
            return ab;
        }).collect(Collectors.toList());
    }

    /**
     * 异常交易提醒 (大额/频繁/可疑)
     * 大额: 单笔超过 5000
     * 频繁: 同一天超过 5 笔
     * 可疑: amount > 0 但 type=EXPENSE（收入类异常）
     */
    public List<TransactionRecord> getSuspiciousTransactions(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                .filter(r -> r.getIsImported() != null && r.getIsImported())
                .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                .filter(r -> r.getTransactionTime() != null)
                .filter(r -> {
                    String ts = r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    return ts.equals(prefix);
                })
                .collect(Collectors.toList());

        // 大额: 单笔超过 5000
        Set<Long> largeAmountIds = records.stream()
                .filter(r -> r.getAmount() != null && r.getAmount().compareTo(new BigDecimal("5000")) > 0)
                .map(TransactionRecord::getId)
                .collect(Collectors.toSet());

        // 频繁: 同一天超过 5 笔
        Map<String, Long> dayCount = records.stream()
                .filter(r -> r.getTransactionTime() != null)
                .collect(Collectors.grouping(
                        r -> r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.counting()
                ));
        Set<String> frequentDays = dayCount.entrySet().stream()
                .filter(e -> e.getValue() > 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Set<Long> frequentIds = records.stream()
                .filter(r -> frequentDays.contains(r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .map(TransactionRecord::getId)
                .collect(Collectors.toSet());

        // 可疑: amount > 0 但 type=EXPENSE
        Set<Long> suspiciousIds = records.stream()
                .filter(r -> r.getAmount() != null && r.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .filter(r -> r.getType() != null && "EXPENSE".equals(r.getType().name()))
                .map(TransactionRecord::getId)
                .collect(Collectors.toSet());

        // 合并
        Set<Long> allSuspicious = new HashSet<>();
        allSuspicious.addAll(largeAmountIds);
        allSuspicious.addAll(frequentIds);
        allSuspicious.addAll(suspiciousIds);

        return records.stream()
                .filter(r -> allSuspicious.contains(r.getId()))
                .collect(Collectors.toList());
    }

    // ======== DTO 类 ========

    @Data
    public static class MonthlyOverview {
        private int year;
        private int month;
        private String income;
        private String expense;
        private String netIncome;
        private int incomeCount;
        private int expenseCount;
    }

    @Data
    public static class MonthlyTrend {
        private int year;
        private int month;
        private String income;
        private String expense;
        private String netCashflow;
    }

    @Data
    public static class CategorySpending {
        private String category;
        private String amount;
    }

    @Data
    public static class PaymentMethodStats {
        private String paymentMethod;
        private String amount;
        private String percent;
    }

    @Data
    public static class AccountBalance {
        private String accountId;
        private String accountName;
        private String accountType;
        private String bankName;
        private String balance;
    }
}