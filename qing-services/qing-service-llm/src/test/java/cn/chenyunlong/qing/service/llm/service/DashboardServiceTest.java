package cn.chenyunlong.qing.service.llm.service;

import cn.chenyunlong.qing.service.llm.dto.DashboardStatsDto;
import cn.chenyunlong.qing.service.llm.entity.Account;
import cn.chenyunlong.qing.service.llm.entity.Category;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.enums.AccountStatusEnum;
import cn.chenyunlong.qing.service.llm.enums.AccountType;
import cn.chenyunlong.qing.service.llm.enums.TransactionType;
import cn.chenyunlong.qing.service.llm.repository.AccountRepository;
import cn.chenyunlong.qing.service.llm.repository.TransactionRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRecordRepository transactionRecordRepository;

    @InjectMocks
    private DashboardService dashboardService;

    // ==================== getMonthlyOverview Tests ====================

    @Test
    @DisplayName("getMonthlyOverview returns correct income/expense for June 2026")
    void testGetMonthlyOverview_Jun2026() {
        // Given: June 2026 with INCOME and EXPENSE records
        TransactionRecord income = createTransaction(1L, new BigDecimal("5000.00"), TransactionType.INCOME,
                LocalDateTime.of(2026, 6, 15, 10, 0));
        TransactionRecord expense = createTransaction(2L, new BigDecimal("2000.00"), TransactionType.EXPENSE,
                LocalDateTime.of(2026, 6, 20, 14, 30));
        TransactionRecord expense2 = createTransaction(3L, new BigDecimal("1500.50"), TransactionType.EXPENSE,
                LocalDateTime.of(2026, 6, 25, 9, 15));

        when(transactionRecordRepository.findAllWithDetailsInTimeRange(any(), any()))
                .thenReturn(List.of(income, expense, expense2));

        // When
        DashboardService.MonthlyOverview result = dashboardService.getMonthlyOverview(2026, 6);

        // Then
        assertEquals(2026, result.getYear());
        assertEquals(6, result.getMonth());
        assertEquals("5000.00", result.getIncome());
        assertEquals("3500.50", result.getExpense());
        assertEquals("1499.50", result.getNetIncome());
        assertEquals(1, result.getIncomeCount());
        assertEquals(2, result.getExpenseCount());
    }

    @Test
    @DisplayName("getMonthlyOverview returns zeros for empty month")
    void testGetMonthlyOverview_EmptyMonth() {
        // Given: no records in the month
        when(transactionRecordRepository.findAllWithDetailsInTimeRange(any(), any()))
                .thenReturn(new ArrayList<>());

        // When
        DashboardService.MonthlyOverview result = dashboardService.getMonthlyOverview(2026, 7);

        // Then
        assertEquals(2026, result.getYear());
        assertEquals(7, result.getMonth());
        assertEquals("0.00", result.getIncome());
        assertEquals("0.00", result.getExpense());
        assertEquals("0.00", result.getNetIncome());
        assertEquals(0, result.getIncomeCount());
        assertEquals(0, result.getExpenseCount());
    }

    // ==================== getYearlyTrend Tests ====================

    @Test
    @DisplayName("getYearlyTrend returns 12 months of data for 2026")
    void testGetYearlyTrend_2026() {
        // Given: records spread across several months in 2026
        TransactionRecord janIncome = createTransaction(1L, new BigDecimal("10000.00"), TransactionType.INCOME,
                LocalDateTime.of(2026, 1, 10, 8, 0));
        TransactionRecord marExpense = createTransaction(2L, new BigDecimal("3000.00"), TransactionType.EXPENSE,
                LocalDateTime.of(2026, 3, 15, 12, 0));
        TransactionRecord junIncome = createTransaction(3L, new BigDecimal("8000.00"), TransactionType.INCOME,
                LocalDateTime.of(2026, 6, 20, 16, 0));
        TransactionRecord junExpense = createTransaction(4L, new BigDecimal("2500.00"), TransactionType.EXPENSE,
                LocalDateTime.of(2026, 6, 22, 10, 0));
        TransactionRecord decExpense = createTransaction(5L, new BigDecimal("500.00"), TransactionType.EXPENSE,
                LocalDateTime.of(2026, 12, 30, 20, 0));

        when(transactionRecordRepository.findAllWithDetailsInTimeRange(any(), any()))
                .thenReturn(List.of(janIncome, marExpense, junIncome, junExpense, decExpense));

        // When
        List<DashboardService.MonthlyTrend> result = dashboardService.getYearlyTrend(2026);

        // Then
        assertNotNull(result);
        assertEquals(12, result.size());

        // Verify January
        DashboardService.MonthlyTrend jan = result.get(0);
        assertEquals(2026, jan.getYear());
        assertEquals(1, jan.getMonth());
        assertEquals("10000.00", jan.getIncome());
        assertEquals("0.00", jan.getExpense());
        assertEquals("10000.00", jan.getNetCashflow());

        // Verify June (month 6)
        DashboardService.MonthlyTrend jun = result.get(5);
        assertEquals(6, jun.getMonth());
        assertEquals("8000.00", jun.getIncome());
        assertEquals("2500.00", jun.getExpense());
        assertEquals("5500.00", jun.getNetCashflow());

        // Verify all 12 months have data
        for (int i = 0; i < 12; i++) {
            assertNotNull(result.get(i));
            assertEquals(i + 1, result.get(i).getMonth());
        }
    }

    // ==================== getCategorySpending Tests ====================

    @Test
    @DisplayName("getCategorySpending groups spending by category name")
    void testGetCategorySpending_GroupsByCategory() {
        // Given: category aggregation results from repository
        List<Object[]> aggregations = new ArrayList<>();
        aggregations.add(new Object[]{"餐饮", new BigDecimal("1500.00")});
        aggregations.add(new Object[]{"交通", new BigDecimal("800.50")});
        aggregations.add(new Object[]{"购物", new BigDecimal("3200.00")});

        when(transactionRecordRepository.getCategorySpendingAggregation(any(), any()))
                .thenReturn(aggregations);

        // When
        List<DashboardService.CategorySpending> result = dashboardService.getCategorySpending(2026, 6);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals("餐饮", result.get(0).getCategory());
        assertEquals("1500.00", result.get(0).getAmount());

        assertEquals("交通", result.get(1).getCategory());
        assertEquals("800.50", result.get(1).getAmount());

        assertEquals("购物", result.get(2).getCategory());
        assertEquals("3200.00", result.get(2).getAmount());
    }

    @Test
    @DisplayName("getCategorySpending handles null category names as 未分类")
    void testGetCategorySpending_NullCategory() {
        // Given: aggregation with null category
        List<Object[]> aggregations = new ArrayList<>();
        aggregations.add(new Object[]{null, new BigDecimal("500.00")});

        when(transactionRecordRepository.getCategorySpendingAggregation(any(), any()))
                .thenReturn(aggregations);

        // When
        List<DashboardService.CategorySpending> result = dashboardService.getCategorySpending(2026, 6);

        // Then
        assertEquals(1, result.size());
        assertEquals("未分类", result.get(0).getCategory());
        assertEquals("500.00", result.get(0).getAmount());
    }

    // ==================== getDashboardStats Tests ====================

    @Test
    @DisplayName("getDashboardStats returns all fields populated")
    void testGetDashboardStats_AllFields() {
        // Given: June 2026 data
        LocalDateTime juneStart = LocalDateTime.of(2026, 6, 1, 0, 0, 0);
        LocalDateTime julyStart = LocalDateTime.of(2026, 7, 1, 0, 0, 0);

        TransactionRecord income = createTransaction(1L, new BigDecimal("10000.00"), TransactionType.INCOME,
                LocalDateTime.of(2026, 6, 15, 10, 0));
        TransactionRecord expense = createTransaction(2L, new BigDecimal("4000.00"), TransactionType.EXPENSE,
                LocalDateTime.of(2026, 6, 20, 14, 30));

        Category foodCategory = new Category();
        foodCategory.setId(1L);
        foodCategory.setName("餐饮");
        expense.setCategory(foodCategory);

        when(transactionRecordRepository.findAllWithDetailsInTimeRange(any(), any()))
                .thenReturn(List.of(income, expense));

        when(transactionRecordRepository.getDailyTrendsAggregation(any(), any()))
                .thenReturn(new ArrayList<>());

        Account account = new Account();
        account.setId(1L);
        account.setAccountName("招商银行储蓄卡");
        account.setAccountType(AccountType.DEBIT);
        account.setBankName("招商银行");
        account.setCardNumber("****1234");
        account.setCurrentBalance(new BigDecimal("50000.00"));
        account.setStatus(AccountStatusEnum.ACTIVE);

        when(accountRepository.findAll()).thenReturn(List.of(account));

        // When
        DashboardStatsDto result = dashboardService.getDashboardStats();

        // Then
        assertNotNull(result);
        assertNotNull(result.getTotalAssets());
        assertEquals(new BigDecimal("50000.00"), result.getTotalAssets());

        assertNotNull(result.getMonthlyIncome());
        assertNotNull(result.getMonthlyExpense());
        assertNotNull(result.getMonthlyBalance());

        assertNotNull(result.getTrends());
        assertNotNull(result.getCategoryStructure());
        assertNotNull(result.getAccounts());
    }

    // ==================== getAccountBalances Tests ====================

    @Test
    @DisplayName("getAccountBalances returns only active accounts")
    void testGetAccountBalances_OnlyActiveAccounts() {
        // Given: mixed active and closed accounts
        Account activeAccount = new Account();
        activeAccount.setId(1L);
        activeAccount.setAccountName("招商银行储蓄卡");
        activeAccount.setAccountType(AccountType.DEBIT);
        activeAccount.setBankName("招商银行");
        activeAccount.setCurrentBalance(new BigDecimal("50000.00"));
        activeAccount.setStatus(AccountStatusEnum.ACTIVE);

        Account closedAccount = new Account();
        closedAccount.setId(2L);
        closedAccount.setAccountName("已关闭账户");
        closedAccount.setCurrentBalance(new BigDecimal("1000.00"));
        closedAccount.setStatus(AccountStatusEnum.CLOSED);

        when(accountRepository.findAll()).thenReturn(List.of(activeAccount, closedAccount));

        // When
        List<DashboardService.AccountBalance> result = dashboardService.getAccountBalances();

        // Then
        assertEquals(1, result.size());
        assertEquals("招商银行储蓄卡", result.get(0).getAccountName());
        assertEquals("50000.00", result.get(0).getBalance());
    }

    // ==================== getTotalAssets Tests ====================

    @Test
    @DisplayName("getTotalAssets sums all account balances")
    void testGetTotalAssets_SumsAllBalances() {
        // Given: multiple accounts
        Account account1 = new Account();
        account1.setCurrentBalance(new BigDecimal("30000.00"));

        Account account2 = new Account();
        account2.setCurrentBalance(new BigDecimal("25000.50"));

        Account account3 = new Account();
        account3.setCurrentBalance(null); // null should be treated as zero

        when(accountRepository.findAll()).thenReturn(List.of(account1, account2, account3));

        // When
        BigDecimal result = dashboardService.getTotalAssets();

        // Then
        assertEquals(new BigDecimal("55000.50"), result);
    }

    // ==================== Helper Methods ====================

    private TransactionRecord createTransaction(Long id, BigDecimal amount, TransactionType type,
                                               LocalDateTime transactionTime) {
        TransactionRecord record = new TransactionRecord();
        record.setId(id);
        record.setAmount(amount);
        record.setTransactionType(type);
        record.setTransactionTime(transactionTime);
        record.setIsImported(true);
        record.setIsDeleted(false);
        return record;
    }
}
