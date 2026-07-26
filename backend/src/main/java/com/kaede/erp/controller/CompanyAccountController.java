package com.kaede.erp.controller;


import com.kaede.erp.common.annotation.OperationLog;
import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.entity.CompanyAccount;
import com.kaede.erp.service.CompanyAccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/company-account")
public class CompanyAccountController {


    private final CompanyAccountService accountService;


    public CompanyAccountController(CompanyAccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    public Result<CompanyAccount> getAccount() {

        return Result.success(
                accountService.getAccount()
        );

    }


    @PutMapping
    @OperationLog(module = "资金管理", operation = "修改", description = "修改企业账户信息")
    public Result<Void> updateAccount(
            @RequestBody Map<String, String> body
    ) {

        accountService.updateAccount(
                body.get("accountName"),
                body.get("remark")
        );

        return Result.success();

    }


    @GetMapping("/today-income")
    public Result<BigDecimal> todayIncome() {

        return Result.success(
                accountService.todayIncome()
        );

    }


    @GetMapping("/today-expense")
    public Result<BigDecimal> todayExpense() {

        return Result.success(
                accountService.todayExpense()
        );

    }


    @GetMapping("/trend")
    public Result<List<Map<String, Object>>> trend() {

        return Result.success(
                accountService.trend()
        );

    }

}
