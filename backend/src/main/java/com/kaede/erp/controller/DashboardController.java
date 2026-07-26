package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.DashboardSummaryDTO;
import com.kaede.erp.dto.DashboardTrendDTO;
import com.kaede.erp.dto.ExpenseSummaryDTO;
import com.kaede.erp.service.DashboardService;
import com.kaede.erp.service.ExpenseService;
import com.kaede.erp.vo.DashboardWarningVO;
import com.kaede.erp.vo.TopProductVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    private final DashboardService dashboardService;

    private final ExpenseService expenseService;


    public DashboardController(DashboardService dashboardService, ExpenseService expenseService) {
        this.dashboardService = dashboardService;
        this.expenseService = expenseService;
    }


    @GetMapping("/summary")
    public Result<DashboardSummaryDTO> summary() {

        return Result.success(
                dashboardService.summary()
        );

    }


    @GetMapping("/trend")
    public Result<List<DashboardTrendDTO>> trend() {

        return Result.success(
                dashboardService.trend()
        );

    }


    @GetMapping("/warnings")
    public Result<List<DashboardWarningVO>> warnings() {

        return Result.success(
                dashboardService.warnings()
        );

    }


    @GetMapping("/top-products")
    public Result<List<TopProductVO>> topProducts() {

        return Result.success(
                dashboardService.topProducts()
        );

    }


    @GetMapping("/expense-summary")
    public Result<ExpenseSummaryDTO> expenseSummary() {

        return Result.success(
                expenseService.summary()
        );

    }

}
