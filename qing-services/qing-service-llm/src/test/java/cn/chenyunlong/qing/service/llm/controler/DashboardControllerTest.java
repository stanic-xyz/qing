package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.DashboardStatsDto;
import cn.chenyunlong.qing.service.llm.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @Test
    public void testGetStats_Returns200() throws Exception {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalAssets(new BigDecimal("100000.00"));
        stats.setMonthlyIncome(new BigDecimal("5000.00"));
        stats.setMonthlyExpense(new BigDecimal("3000.00"));
        stats.setMonthlyBalance(new BigDecimal("2000.00"));

        when(dashboardService.getDashboardStats()).thenReturn(stats);

        mockMvc.perform(get("/api/finance/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.totalAssets").value(100000.00))
                .andExpect(jsonPath("$.data.monthlyIncome").value(5000.00))
                .andExpect(jsonPath("$.data.monthlyExpense").value(3000.00))
                .andExpect(jsonPath("$.data.monthlyBalance").value(2000.00));
    }

    @Test
    public void testGetMonthlyOverview_Returns200() throws Exception {
        DashboardService.MonthlyOverview overview = new DashboardService.MonthlyOverview();
        overview.setYear(2026);
        overview.setMonth(4);
        overview.setIncome("5000.00");
        overview.setExpense("3000.00");
        overview.setNetIncome("2000.00");
        overview.setIncomeCount(5);
        overview.setExpenseCount(10);

        when(dashboardService.getMonthlyOverview(2026, 4)).thenReturn(overview);

        mockMvc.perform(get("/api/finance/dashboard/monthly")
                        .param("year", "2026")
                        .param("month", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.year").value(2026))
                .andExpect(jsonPath("$.data.month").value(4))
                .andExpect(jsonPath("$.data.income").value("5000.00"))
                .andExpect(jsonPath("$.data.expense").value("3000.00"))
                .andExpect(jsonPath("$.data.netIncome").value("2000.00"))
                .andExpect(jsonPath("$.data.incomeCount").value(5))
                .andExpect(jsonPath("$.data.expenseCount").value(10));
    }

    @Test
    public void testGetYearlyTrend_Returns200() throws Exception {
        DashboardService.MonthlyTrend trend = new DashboardService.MonthlyTrend();
        trend.setYear(2026);
        trend.setMonth(1);
        trend.setIncome("10000.00");
        trend.setExpense("6000.00");
        trend.setNetCashflow("4000.00");

        when(dashboardService.getYearlyTrend(2026)).thenReturn(List.of(trend));

        mockMvc.perform(get("/api/finance/dashboard/yearly-trend")
                        .param("year", "2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].year").value(2026))
                .andExpect(jsonPath("$.data[0].month").value(1))
                .andExpect(jsonPath("$.data[0].income").value("10000.00"))
                .andExpect(jsonPath("$.data[0].expense").value("6000.00"))
                .andExpect(jsonPath("$.data[0].netCashflow").value("4000.00"));
    }

    @Test
    public void testGetCategorySpending_Returns200() throws Exception {
        DashboardService.CategorySpending spending = new DashboardService.CategorySpending();
        spending.setCategory("餐饮");
        spending.setAmount("1500.00");

        when(dashboardService.getCategorySpending(2026, 4)).thenReturn(List.of(spending));

        mockMvc.perform(get("/api/finance/dashboard/category-spending")
                        .param("year", "2026")
                        .param("month", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].category").value("餐饮"))
                .andExpect(jsonPath("$.data[0].amount").value("1500.00"));
    }

    @Test
    public void testGetAccountBalances_Returns200() throws Exception {
        DashboardService.AccountBalance balance = new DashboardService.AccountBalance();
        balance.setAccountId("1");
        balance.setAccountName("主账户");
        balance.setAccountType("CHECKING");
        balance.setBankName("测试银行");
        balance.setBalance("50000.00");

        when(dashboardService.getAccountBalances()).thenReturn(List.of(balance));

        mockMvc.perform(get("/api/finance/dashboard/account-balances"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].accountId").value("1"))
                .andExpect(jsonPath("$.data[0].accountName").value("主账户"))
                .andExpect(jsonPath("$.data[0].accountType").value("CHECKING"))
                .andExpect(jsonPath("$.data[0].bankName").value("测试银行"))
                .andExpect(jsonPath("$.data[0].balance").value("50000.00"));
    }
}
