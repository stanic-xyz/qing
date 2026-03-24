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

@RestController
@RequestMapping("/api/finance/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private TransactionRecordRepository transactionRepo;

    @GetMapping("/stats")
    public Result<DashboardStatsDTO> getStats() {
        List<TransactionRecord> allRecords = transactionRepo.findAll()
                .stream().filter(r -> !r.getIsDeleted()).collect(Collectors.toList());

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        
        Map<String, DailyTrend> trendMap = new TreeMap<>(); // 按日期排序

        for (TransactionRecord r : allRecords) {
            if (r.getAmount() == null || r.getTransactionTime() == null) continue;
            
            String dateKey = r.getTransactionTime().toLocalDate().toString();
            DailyTrend trend = trendMap.computeIfAbsent(dateKey, k -> new DailyTrend(dateKey, BigDecimal.ZERO, BigDecimal.ZERO));
            
            if ("INCOME".equals(r.getType())) {
                totalIncome = totalIncome.add(r.getAmount());
                trend.setIncome(trend.getIncome().add(r.getAmount()));
            } else if ("EXPENSE".equals(r.getType())) {
                totalExpense = totalExpense.add(r.getAmount());
                trend.setExpense(trend.getExpense().add(r.getAmount()));
            }
        }

        DashboardStatsDTO dto = new DashboardStatsDTO();
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        dto.setBalance(totalIncome.subtract(totalExpense));
        dto.setTrends(new ArrayList<>(trendMap.values()));

        return Result.success(dto);
    }

    @Data
    public static class DashboardStatsDTO {
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal balance;
        private List<DailyTrend> trends;
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
}