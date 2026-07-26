package com.kaede.erp.ai.service;


import com.kaede.erp.service.DashboardService;
import com.kaede.erp.service.InventoryService;
import com.kaede.erp.dto.ExpenseSummaryDTO;
import com.kaede.erp.service.ExpenseService;
import com.kaede.erp.vo.DashboardWarningVO;
import com.kaede.erp.vo.TopProductVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ContextService {


    private final DashboardService dashboardService;

    private final InventoryService inventoryService;

    private final ExpenseService expenseService;

    private final JdbcTemplate jdbcTemplate;


    public ContextService(
            DashboardService dashboardService,
            InventoryService inventoryService,
            ExpenseService expenseService,
            JdbcTemplate jdbcTemplate
    ) {
        this.dashboardService = dashboardService;
        this.inventoryService = inventoryService;
        this.expenseService = expenseService;
        this.jdbcTemplate = jdbcTemplate;
    }


    public Map<String, Object> buildSalesContext(String range) {

        String startDate = switch (range) {
            case "WEEK" -> LocalDate.now().minusDays(7).toString();
            case "MONTH" -> LocalDate.now().minusDays(30).toString();
            case "QUARTER" -> LocalDate.now().minusDays(90).toString();
            default -> LocalDate.now().minusDays(30).toString();
        };


        Map<String, Object> salesSummary = jdbcTemplate.queryForMap(
                "SELECT COALESCE(SUM(total_amount),0) AS total, COUNT(*) AS cnt, " +
                "COALESCE(AVG(total_amount),0) AS avg_amount FROM sales_order " +
                "WHERE status='COMPLETED' AND create_time >= ?",
                startDate
        );

        List<Map<String, Object>> daily = jdbcTemplate.queryForList(
                "SELECT DATE(create_time) AS day, COALESCE(SUM(total_amount),0) AS amount " +
                "FROM sales_order WHERE status='COMPLETED' AND create_time >= ? " +
                "GROUP BY DATE(create_time) ORDER BY day",
                startDate
        );

        List<TopProductVO> top = dashboardService.topProducts();


        Map<String, Object> monthSales = jdbcTemplate.queryForMap(
                "SELECT COALESCE(SUM(total_amount),0) AS month_amount FROM sales_order " +
                "WHERE status='COMPLETED' AND create_time >= ?",
                LocalDate.now().withDayOfMonth(1).toString()
        );

        Map<String, Object> prevMonth = jdbcTemplate.queryForMap(
                "SELECT COALESCE(SUM(total_amount),0) AS prev_amount FROM sales_order " +
                "WHERE status='COMPLETED' AND create_time >= ? AND create_time < ?",
                LocalDate.now().minusMonths(1).withDayOfMonth(1).toString(),
                LocalDate.now().withDayOfMonth(1).toString()
        );


        Map<String, Object> ctx = new HashMap<>();
        ctx.put("range", range);
        ctx.put("totalAmount", salesSummary.get("total"));
        ctx.put("orderCount", salesSummary.get("cnt"));
        ctx.put("avgOrderAmount", salesSummary.get("avg_amount"));
        ctx.put("dailyTrend", daily.stream()
                .map(d -> d.get("day") + ": " + d.get("amount") + "元")
                .collect(Collectors.joining("\n")));
        ctx.put("topProducts", top.stream()
                .map(p -> p.getProductName() + " x" + p.getSaleQuantity() + "件")
                .collect(Collectors.joining("\n")));
        ctx.put("monthAmount", monthSales.get("month_amount"));
        ctx.put("prevMonthAmount", prevMonth.get("prev_amount"));

        Number monthAmt = (Number) monthSales.get("month_amount");
        Number prevAmt = (Number) prevMonth.get("prev_amount");
        double growth = prevAmt.doubleValue() > 0
                ? (monthAmt.doubleValue() - prevAmt.doubleValue()) / prevAmt.doubleValue() * 100
                : 0;
        ctx.put("growthRate", String.format("%.1f%%", growth));

        return ctx;
    }


    public Map<String, Object> buildInventoryContext() {

        var summary = dashboardService.summary();
        var expenseSummary = expenseService.summary();

        List<DashboardWarningVO> warnings = dashboardService.warnings();

        List<Map<String, Object>> records = jdbcTemplate.queryForList(
                "SELECT r.type, r.change_qty, r.create_time, p.name AS product_name " +
                "FROM inventory_record r JOIN product p ON r.product_id = p.id " +
                "ORDER BY r.create_time DESC LIMIT 20"
        );


        Map<String, Object> ctx = new HashMap<>();
        ctx.put("productCount", summary.getProductCount());
        ctx.put("totalQuantity", summary.getInventoryQuantity());
        ctx.put("lowStockCount", summary.getLowStockCount());
        ctx.put("warnings", warnings.stream()
                .map(w -> w.getProductName() + ": " + w.getQuantity() + "/" + w.getWarningValue())
                .collect(Collectors.joining("\n")));
        ctx.put("recentRecords", records.stream()
                .map(r -> r.get("product_name") + " " + r.get("type") + " " + r.get("change_qty"))
                .collect(Collectors.joining("\n")));
        ctx.put("companyBalance", expenseSummary.getCompanyBalance());
        ctx.put("monthExpense", expenseSummary.getMonthExpense());

        return ctx;
    }


    public Map<String, Object> buildOverviewContext() {

        var summary = dashboardService.summary();
        var trend = dashboardService.trend();
        var warnings = dashboardService.warnings();
        var top = dashboardService.topProducts();
        var expenseSummary = expenseService.summary();


        String salesTrend = trend.stream()
                .map(t -> t.getDate() + ": sale=" + t.getSaleAmount())
                .collect(Collectors.joining("\n"));

        String purchaseTrend = trend.stream()
                .map(t -> t.getDate() + ": purchase=" + t.getPurchaseAmount())
                .collect(Collectors.joining("\n"));


        Map<String, Object> ctx = new HashMap<>();
        ctx.put("productCount", summary.getProductCount());
        ctx.put("customerCount", summary.getCustomerCount());
        ctx.put("supplierCount", summary.getSupplierCount());
        ctx.put("totalQuantity", summary.getInventoryQuantity());
        ctx.put("todaySaleAmount", summary.getTodaySaleAmount());
        ctx.put("todayPurchaseAmount", summary.getTodayPurchaseAmount());
        ctx.put("salesTrend", salesTrend);
        ctx.put("purchaseTrend", purchaseTrend);
        ctx.put("lowStockCount", summary.getLowStockCount());
        ctx.put("warnings", warnings.stream()
                .map(w -> w.getProductName() + ": " + w.getQuantity() + "/" + w.getWarningValue())
                .collect(Collectors.joining("\n")));
        ctx.put("topProducts", top.stream()
                .map(p -> p.getProductName() + " x" + p.getSaleQuantity() + "件")
                .collect(Collectors.joining("\n")));
        ctx.put("monthExpense", expenseSummary.getMonthExpense());
        ctx.put("pendingExpenseCount", expenseSummary.getPendingCount());
        ctx.put("companyBalance", expenseSummary.getCompanyBalance());

        return ctx;
    }

}
