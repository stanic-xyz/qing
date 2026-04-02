package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/finance/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private TransactionRecordRepository transactionRepo;
    
    @Autowired
    private AccountRepository accountRepo;

    @GetMapping("/stats")
    public Result<DashboardStatsDTO> getStats() {
        // 1. 获取所有状态正常的账户，计算总资产
        List<Account> activeAccounts = accountRepo.findAll().stream()
                .filter(a -> "ACTIVE".equals(a.getStatus()))
                .collect(Collectors.toList());
        
        BigDecimal totalAssets = BigDecimal.ZERO;
        for (Account acc : activeAccounts) {
            if (acc.getCurrentBalance() != null) {
                totalAssets = totalAssets.add(acc.getCurrentBalance());
            }
        }

        // 2. 获取近 30 天的流水
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        YearMonth currentMonth = YearMonth.now();

        List<TransactionRecord> allRecords = transactionRepo.findAll()
                .stream().filter(r -> !r.getIsDeleted() && (r.getIsImported() == null || r.getIsImported()))
                .filter(r -> r.getTransactionTime() != null && r.getTransactionTime().toLocalDate().isAfter(thirtyDaysAgo.minusDays(1)))
                .collect(Collectors.toList());

        BigDecimal monthlyIncome = BigDecimal.ZERO;
        BigDecimal monthlyExpense = BigDecimal.ZERO;
        
        Map<String, DailyTrend> trendMap = new TreeMap<>(); // 按日期排序
        
        // 预先填充近 30 天的日期，确保图表没有断层
        for (int i = 0; i <= 30; i++) {
            String d = thirtyDaysAgo.plusDays(i).toString();
            trendMap.put(d, new DailyTrend(d, BigDecimal.ZERO, BigDecimal.ZERO));
        }

        Map<String, BigDecimal> categoryStructure = new HashMap<>();

        for (TransactionRecord r : allRecords) {
            if (r.getAmount() == null) continue;
            
            LocalDate txDate = r.getTransactionTime().toLocalDate();
            String dateKey = txDate.toString();
            DailyTrend trend = trendMap.get(dateKey);
            
            boolean isCurrentMonth = YearMonth.from(txDate).equals(currentMonth);

            if ("INCOME".equals(r.getType())) {
                if (trend != null) {
                    trend.setIncome(trend.getIncome().add(r.getAmount()));
                }
                if (isCurrentMonth) {
                    monthlyIncome = monthlyIncome.add(r.getAmount());
                }
            } else if ("EXPENSE".equals(r.getType())) {
                if (trend != null) {
                    trend.setExpense(trend.getExpense().add(r.getAmount()));
                }
                if (isCurrentMonth) {
                    monthlyExpense = monthlyExpense.add(r.getAmount());
                    String cat = r.getCategory() != null && !r.getCategory().isEmpty() ? r.getCategory() : "未分类";
                    categoryStructure.put(cat, categoryStructure.getOrDefault(cat, BigDecimal.ZERO).add(r.getAmount()));
                }
            }
        }

        List<CategoryStat> categoryList = categoryStructure.entrySet().stream()
                .map(e -> new CategoryStat(e.getKey(), e.getValue()))
                .sorted((a, b) -> b.getAmount().compareTo(a.getAmount()))
                .collect(Collectors.toList());

        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTotalAssets(totalAssets);
        dto.setMonthlyIncome(monthlyIncome);
        dto.setMonthlyExpense(monthlyExpense);
        dto.setMonthlyBalance(monthlyIncome.subtract(monthlyExpense));
        dto.setTrends(new ArrayList<>(trendMap.values()));
        dto.setCategoryStructure(categoryList);
        dto.setAccounts(activeAccounts);

        return Result.success(dto);
    }

    @Data
    public static class DashboardStatsDTO {
        private BigDecimal totalAssets;
        private BigDecimal monthlyIncome;
        private BigDecimal monthlyExpense;
        private BigDecimal monthlyBalance;
        private List<DailyTrend> trends;
        private List<CategoryStat> categoryStructure;
        private List<Account> accounts;
    }

    @Data
    public static class DailyTrend {
        private String date;
        private BigDecimal income;
        private BigDecimal expense;
        
        public DailyTrend(String date, BigDecimal income, BigDecimal expense) {
            this.date = date;
            this.income = income;
            this.expense = expense;
        }
    }
    
    @Data
    public static class CategoryStat {
        private String name;
        private BigDecimal value; // Recharts pie chart defaults to 'name' and 'value' fields
        private BigDecimal amount;
        
        public CategoryStat(String name, BigDecimal amount) {
            this.name = name;
            this.value = amount;
            this.amount = amount;
        }
    }
}