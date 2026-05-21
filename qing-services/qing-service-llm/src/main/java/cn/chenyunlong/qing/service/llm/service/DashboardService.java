package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Channel;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.dto.DashboardStatsDto;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    /**
     * 月度收支汇总 (按月统计收入/支出/笔数)
     * 优化后：使用时间范围查询，避免全表查询
     */
    public MonthlyOverview getMonthlyOverview(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

        // 使用优化的查询方法，只获取当月数据
        List<TransactionRecord> records = transactionRecordRepository
                .findAllWithDetailsInTimeRange(start, end);

        // 在内存中聚合
        return calculateMonthlyOverviewWithDetails(records, year, month);
    }

    /**
     * 计算月度收支汇总（带详细信息）
     */
    private MonthlyOverview calculateMonthlyOverviewWithDetails(List<TransactionRecord> records, int year, int month) {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        int incomeCount = 0;
        int expenseCount = 0;

        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            if (r.getTransactionType() != null && "INCOME".equals(r.getTransactionType().name())) {
                income = income.add(r.getAmount());
                incomeCount++;
            } else if (r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name())) {
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
     * 优化后：使用数据库聚合，一次查询获取全年数据
     */
    public List<MonthlyTrend> getYearlyTrend(int year) {
        YearMonth start = YearMonth.of(year, 1);
        YearMonth end = YearMonth.of(year, 12).plusMonths(1);
        LocalDateTime startTime = start.atDay(1).atStartOfDay();
        LocalDateTime endTime = end.atDay(1).atStartOfDay();

        // 单次查询获取全年数据
        List<TransactionRecord> records = transactionRecordRepository
                .findAllWithDetailsInTimeRange(startTime, endTime);

        // 按月聚合
        Map<Integer, Map<String, BigDecimal>> monthlyData = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) {
            monthlyData.put(m, new HashMap<>() {{
                put("income", BigDecimal.ZERO);
                put("expense", BigDecimal.ZERO);
            }});
        }

        for (TransactionRecord r : records) {
            if (r.getAmount() == null || r.getTransactionTime() == null) continue;
            int month = r.getTransactionTime().getMonthValue();

            Map<String, BigDecimal> monthStats = monthlyData.get(month);
            if (monthStats != null) {
                if (r.getTransactionType() != null && "INCOME".equals(r.getTransactionType().name())) {
                    monthStats.merge("income", r.getAmount(), BigDecimal::add);
                } else if (r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name())) {
                    monthStats.merge("expense", r.getAmount(), BigDecimal::add);
                }
            }
        }

        List<MonthlyTrend> trends = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            Map<String, BigDecimal> monthStats = monthlyData.get(m);
            BigDecimal income = monthStats.get("income");
            BigDecimal expense = monthStats.get("expense");

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
     * 优化后：使用数据库聚合查询
     */
    public List<CategorySpending> getCategorySpending(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

        // 使用数据库聚合查询
        List<Object[]> aggregations = transactionRecordRepository
                .getCategorySpendingAggregation(start, end);

        return aggregations.stream()
                .map(row -> {
                    CategorySpending cs = new CategorySpending();
                    cs.setCategory(row[0] != null ? row[0].toString() : "未分类");
                    BigDecimal amount = row[1] instanceof BigDecimal
                            ? (BigDecimal) row[1]
                            : new BigDecimal(row[1].toString());
                    cs.setAmount(amount.setScale(2, RoundingMode.HALF_UP).toString());
                    return cs;
                })
                .collect(Collectors.toList());
    }

    /**
     * 支付方式统计 (各渠道/账户消费占比)
     * 优化后：使用时间范围查询 + 预加载关联实体
     */
    public List<PaymentMethodStats> getPaymentMethodStats(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

        // 使用优化的查询方法，预加载 Account 和 Channel
        List<TransactionRecord> records = transactionRecordRepository
                .findAllWithDetailsInTimeRange(start, end);

        BigDecimal total = records.stream()
                .filter(r -> r.getAmount() != null)
                .map(TransactionRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> methodMap = new LinkedHashMap<>();
        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            String method;
            Account account = r.getAccount();
            if (account != null) {
                Channel accountChannel = account.getChannel();
                if (accountChannel != null && accountChannel.getName() != null) {
                    method = accountChannel.getName();
                } else if (account.getAccountName() != null) {
                    method = account.getAccountName();
                } else {
                    method = "其他";
                }
            } else {
                method = "其他";
            }
            methodMap.merge(method, r.getAmount(), BigDecimal::add);
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
     * 优化后：单次查询获取所有数据，内存中按月聚合
     */
    public List<MonthlyTrend> getTrend(int months) {
        YearMonth current = YearMonth.now();
        YearMonth startMonth = current.minusMonths(months - 1);
        LocalDateTime startTime = startMonth.atDay(1).atStartOfDay();
        LocalDateTime endTime = current.plusMonths(1).atDay(1).atStartOfDay();

        // 单次查询获取所有数据
        List<TransactionRecord> records = transactionRecordRepository
                .findAllWithDetailsInTimeRange(startTime, endTime);

        // 按月聚合
        Map<YearMonth, Map<String, BigDecimal>> monthlyData = new LinkedHashMap<>();
        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = current.minusMonths(i);
            monthlyData.put(ym, new HashMap<>() {{
                put("income", BigDecimal.ZERO);
                put("expense", BigDecimal.ZERO);
            }});
        }

        for (TransactionRecord r : records) {
            if (r.getAmount() == null || r.getTransactionTime() == null) continue;
            YearMonth ym = YearMonth.from(r.getTransactionTime());

            Map<String, BigDecimal> monthStats = monthlyData.get(ym);
            if (monthStats != null) {
                if (r.getTransactionType() != null && "INCOME".equals(r.getTransactionType().name())) {
                    monthStats.merge("income", r.getAmount(), BigDecimal::add);
                } else if (r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name())) {
                    monthStats.merge("expense", r.getAmount(), BigDecimal::add);
                }
            }
        }

        List<MonthlyTrend> trends = new ArrayList<>();
        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = current.minusMonths(i);
            Map<String, BigDecimal> monthStats = monthlyData.get(ym);
            BigDecimal income = monthStats.get("income");
            BigDecimal expense = monthStats.get("expense");

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
                .toList();

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
     * 优化后：使用时间范围查询
     */
    public List<TransactionRecord> getSuspiciousTransactions(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

        // 使用优化的查询方法
        List<TransactionRecord> records = transactionRecordRepository
                .findAllWithDetailsInTimeRange(start, end);

        // 大额: 单笔超过 5000
        Set<Long> largeAmountIds = records.stream()
                .filter(r -> r.getAmount() != null && r.getAmount().compareTo(new BigDecimal("5000")) > 0)
                .map(TransactionRecord::getId)
                .collect(Collectors.toSet());

        // 频繁: 同一天超过 5 笔
        Map<String, Long> dayCount = records.stream()
                .filter(r -> r.getTransactionTime() != null)
                .collect(Collectors.groupingBy(
                        (TransactionRecord r) -> r.getTransactionTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
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
                .filter(r -> r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name()))
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

    // ======== DashboardStats 聚合方法 ========

    /**
     * 系统总资产 = 所有账户当前余额之和
     */
    public BigDecimal getTotalAssets() {
        return accountRepository.findAll().stream()
                .map(Account::getCurrentBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 近N天每日收支趋势
     */
    public List<DashboardStatsDto.DailyTrend> getDailyTrends(int days) {
        LocalDate today = LocalDate.now();
        List<DashboardStatsDto.DailyTrend> trends = new java.util.ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String prefix = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            List<TransactionRecord> dayRecords = transactionRecordRepository.findAll().stream()
                    .filter(r -> r.getIsImported() != null && r.getIsImported())
                    .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                    .filter(r -> r.getTransactionTime() != null)
                    .filter(r -> {
                        LocalDateTime t = r.getTransactionTime();
                        return !t.isBefore(start) && t.isBefore(end);
                    })
                    .toList();

            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;
            for (TransactionRecord r : dayRecords) {
                if (r.getAmount() == null) continue;
                if (r.getTransactionType() != null && "INCOME".equals(r.getTransactionType().name())) {
                    income = income.add(r.getAmount());
                } else if (r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name())) {
                    expense = expense.add(r.getAmount());
                }
            }

            DashboardStatsDto.DailyTrend t = new DashboardStatsDto.DailyTrend();
            t.setDate(prefix);
            t.setIncome(income);
            t.setExpense(expense);
            trends.add(t);
        }
        return trends;
    }

    /**
     * 本月支出分类饼图数据
     */
    public List<DashboardStatsDto.CategoryPie> getCategoryPie(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        String prefix = ym.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

        List<TransactionRecord> records = transactionRecordRepository.findAll().stream()
                .filter(r -> r.getIsImported() != null && r.getIsImported())
                .filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
                .filter(r -> r.getTransactionTime() != null)
                .filter(r -> r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name()))
                .filter(r -> {
                    LocalDateTime t = r.getTransactionTime();
                    return !t.isBefore(start) && t.isBefore(end);
                })
                .toList();

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
                    DashboardStatsDto.CategoryPie p = new DashboardStatsDto.CategoryPie();
                    p.setCategory(e.getKey());
                    p.setValue(e.getValue());
                    return p;
                })
                .collect(Collectors.toList());
    }

    /**
     * 聚合 DashboardStats（供 /stats 接口使用）
     * 优化后：只需 2-3 次数据库查询（交易表1-2次 + 账户表1次）
     */
    public DashboardStatsDto getDashboardStats() {
        YearMonth current = YearMonth.now();
        LocalDateTime monthStart = current.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = current.plusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime trendsStart = LocalDate.now().minusDays(30).atStartOfDay();

        // 1. 单次查询获取当月所有交易数据（带关联实体预加载）
        List<TransactionRecord> monthRecords = transactionRecordRepository
                .findAllWithDetailsInTimeRange(monthStart, monthEnd);

        // 2. 单次查询获取近30天趋势数据（使用数据库聚合）
        List<Object[]> dailyAggregations = transactionRecordRepository
                .getDailyTrendsAggregation(trendsStart, monthEnd);
        List<DashboardStatsDto.DailyTrend> trends = aggregateDailyTrends(dailyAggregations, 30);

        // 3. 查询账户总资产和账户列表（一次查询，复用结果）
        List<Account> activeAccounts = accountRepository.findAll().stream()
                .filter(a -> a.getStatus() != null && "ACTIVE".equals(a.getStatus().name()))
                .toList();

        BigDecimal totalAssets = activeAccounts.stream()
                .map(Account::getCurrentBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 在内存中聚合月度数据（针对已加载的数据）
        MonthlyOverview monthly = calculateMonthlyOverview(monthRecords);

        // 5. 计算分类支出（针对已加载的数据）
        List<DashboardStatsDto.CategoryPie> categoryStructure = calculateCategoryPie(monthRecords);

        // 6. 转换账户列表
        List<DashboardStatsDto.AccountBalanceDto> accounts = activeAccounts.stream()
                .map(this::mapToAccountDto)
                .collect(Collectors.toList());

        // 7. 组装结果
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalAssets(totalAssets);
        stats.setMonthlyIncome(parseBd(monthly.getIncome()));
        stats.setMonthlyExpense(parseBd(monthly.getExpense()));
        stats.setMonthlyBalance(parseBd(monthly.getNetIncome()));
        stats.setTrends(trends);
        stats.setCategoryStructure(categoryStructure);
        stats.setAccounts(accounts);
        return stats;
    }

    /**
     * 计算月度收支汇总
     */
    private MonthlyOverview calculateMonthlyOverview(List<TransactionRecord> records) {
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        int incomeCount = 0;
        int expenseCount = 0;

        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            if (r.getTransactionType() != null && "INCOME".equals(r.getTransactionType().name())) {
                income = income.add(r.getAmount());
                incomeCount++;
            } else if (r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name())) {
                expense = expense.add(r.getAmount());
                expenseCount++;
            }
        }

        MonthlyOverview dto = new MonthlyOverview();
        dto.setIncome(income.setScale(2, RoundingMode.HALF_UP).toString());
        dto.setExpense(expense.setScale(2, RoundingMode.HALF_UP).toString());
        dto.setNetIncome(income.subtract(expense).setScale(2, RoundingMode.HALF_UP).toString());
        dto.setIncomeCount(incomeCount);
        dto.setExpenseCount(expenseCount);
        return dto;
    }

    /**
     * 从数据库聚合结果计算每日趋势
     */
    private List<DashboardStatsDto.DailyTrend> aggregateDailyTrends(List<Object[]> aggregations, int days) {
        Map<LocalDate, BigDecimal> incomeMap = new HashMap<>();
        Map<LocalDate, BigDecimal> expenseMap = new HashMap<>();
        LocalDate today = LocalDate.now();

        // 初始化最近N天的数据
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            incomeMap.put(date, BigDecimal.ZERO);
            expenseMap.put(date, BigDecimal.ZERO);
        }

        // 填充聚合数据
        for (Object[] row : aggregations) {
            if (row.length >= 3) {
                LocalDate date = row[0] instanceof java.sql.Date
                        ? ((java.sql.Date) row[0]).toLocalDate()
                        : (LocalDate) row[0];
                String type = row[1] != null ? row[1].toString() : "";
                BigDecimal amount = row[2] instanceof BigDecimal
                        ? (BigDecimal) row[2]
                        : new BigDecimal(row[2].toString());

                if ("INCOME".equals(type)) {
                    incomeMap.merge(date, amount, BigDecimal::add);
                } else if ("EXPENSE".equals(type)) {
                    expenseMap.merge(date, amount, BigDecimal::add);
                }
            }
        }

        // 生成趋势列表
        List<DashboardStatsDto.DailyTrend> trends = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            DashboardStatsDto.DailyTrend t = new DashboardStatsDto.DailyTrend();
            t.setDate(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            t.setIncome(incomeMap.getOrDefault(date, BigDecimal.ZERO));
            t.setExpense(expenseMap.getOrDefault(date, BigDecimal.ZERO));
            trends.add(t);
        }

        return trends;
    }

    /**
     * 计算分类支出饼图
     */
    private List<DashboardStatsDto.CategoryPie> calculateCategoryPie(List<TransactionRecord> records) {
        Map<String, BigDecimal> categoryMap = new LinkedHashMap<>();

        for (TransactionRecord r : records) {
            if (r.getAmount() == null) continue;
            if (r.getTransactionType() != null && "EXPENSE".equals(r.getTransactionType().name())) {
                String catName = (r.getCategory() != null && r.getCategory().getName() != null)
                        ? r.getCategory().getName() : "未分类";
                categoryMap.merge(catName, r.getAmount(), BigDecimal::add);
            }
        }

        return categoryMap.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(e -> {
                    DashboardStatsDto.CategoryPie p = new DashboardStatsDto.CategoryPie();
                    p.setCategory(e.getKey());
                    p.setValue(e.getValue());
                    return p;
                })
                .collect(Collectors.toList());
    }

    /**
     * 将 Account 实体转换为 DTO
     */
    private DashboardStatsDto.AccountBalanceDto mapToAccountDto(Account a) {
        DashboardStatsDto.AccountBalanceDto dto = new DashboardStatsDto.AccountBalanceDto();
        dto.setId(a.getId());
        dto.setAccountName(a.getAccountName());
        dto.setBankName(a.getBankName());
        dto.setCardNumber(a.getCardNumber());
        dto.setCurrentBalance(a.getCurrentBalance() != null
                ? a.getCurrentBalance().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        dto.setChannelIcon(a.getIcon());
        return dto;
    }

    private BigDecimal parseBd(String s) {
        if (s == null || s.isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
