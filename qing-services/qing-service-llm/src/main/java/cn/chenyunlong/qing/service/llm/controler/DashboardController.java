package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.dto.DashboardStatsDto;
import cn.chenyunlong.qing.service.llm.dto.Result;
import cn.chenyunlong.qing.service.llm.entity.TransactionRecord;
import cn.chenyunlong.qing.service.llm.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 全局看板聚合数据
     * GET /api/finance/dashboard/stats
     */
    @GetMapping("/stats")
    public Result<DashboardStatsDto> getStats() {
        return Result.success(dashboardService.getDashboardStats());
    }

    /**
     * 月度收支汇总 (按月统计收入/支出/笔数)
     * GET /api/finance/dashboard/monthly?year=2026&month=4
     */
    @GetMapping("/monthly")
    public Result<DashboardService.MonthlyOverview> getMonthlyOverview(
            @RequestParam int year,
            @RequestParam int month) {
        return Result.success(dashboardService.getMonthlyOverview(year, month));
    }

    /**
     * 年度趋势 (近12个月的收入/支出/净现金流)
     * GET /api/finance/dashboard/yearly-trend?year=2026
     */
    @GetMapping("/yearly-trend")
    public Result<List<DashboardService.MonthlyTrend>> getYearlyTrend(
            @RequestParam int year) {
        return Result.success(dashboardService.getYearlyTrend(year));
    }

    /**
     * 分类支出排行 (指定月份各分类支出金额)
     * GET /api/finance/dashboard/category-spending?year=2026&month=4
     */
    @GetMapping("/category-spending")
    public Result<List<DashboardService.CategorySpending>> getCategorySpending(
            @RequestParam int year,
            @RequestParam int month) {
        return Result.success(dashboardService.getCategorySpending(year, month));
    }

    /**
     * 支付方式统计 (各渠道/账户消费占比)
     * GET /api/finance/dashboard/payment-methods?year=2026&month=4
     */
    @GetMapping("/payment-methods")
    public Result<List<DashboardService.PaymentMethodStats>> getPaymentMethodStats(
            @RequestParam int year,
            @RequestParam int month) {
        return Result.success(dashboardService.getPaymentMethodStats(year, month));
    }

    /**
     * 收支趋势 (近N个月)
     * GET /api/finance/dashboard/trend?months=12
     */
    @GetMapping("/trend")
    public Result<List<DashboardService.MonthlyTrend>> getTrend(
            @RequestParam(defaultValue = "12") int months) {
        return Result.success(dashboardService.getTrend(months));
    }

    /**
     * 账户余额概览 (所有账户当前余额)
     * GET /api/finance/dashboard/account-balances
     */
    @GetMapping("/account-balances")
    public Result<List<DashboardService.AccountBalance>> getAccountBalances() {
        return Result.success(dashboardService.getAccountBalances());
    }

    /**
     * 异常交易提醒 (大额/频繁/可疑)
     * GET /api/finance/dashboard/suspicious?year=2026&month=4
     */
    @GetMapping("/suspicious")
    public Result<List<TransactionRecord>> getSuspiciousTransactions(
            @RequestParam int year,
            @RequestParam int month) {
        return Result.success(dashboardService.getSuspiciousTransactions(year, month));
    }
}