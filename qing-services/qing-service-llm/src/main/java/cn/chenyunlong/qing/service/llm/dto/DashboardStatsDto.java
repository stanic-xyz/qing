package cn.chenyunlong.qing.service.llm.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardStatsDto {
    private BigDecimal totalAssets;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private BigDecimal monthlyBalance;
    private List<DailyTrend> trends;
    private List<CategoryPie> categoryStructure;
    private List<AccountBalanceDto> accounts;

    @Data
    public static class DailyTrend {
        private String date;
        private BigDecimal income;
        private BigDecimal expense;
    }

    @Data
    public static class CategoryPie {
        private String category;
        private BigDecimal value;
    }

    @Data
    public static class AccountBalanceDto {
        private Long id;
        private String accountName;
        private String bankName;
        private String cardNumber;
        private BigDecimal currentBalance;
        private String channelIcon;
    }
}
