package com.kaede.erp.controller;


import com.kaede.erp.common.annotation.OperationLog;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.ExpenseCreateRequest;
import com.kaede.erp.dto.ExpenseQueryRequest;
import com.kaede.erp.dto.ExpenseUpdateRequest;
import com.kaede.erp.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {


    private final ExpenseService expenseService;


    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @GetMapping
    public Result<List<Map<String, Object>>> list(
            ExpenseQueryRequest req
    ) {

        return Result.success(
                expenseService.list(req)
        );

    }


    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(
            @PathVariable Long id
    ) {

        return Result.success(
                expenseService.detail(id)
        );

    }


    @PostMapping
    @OperationLog(module = "费用管理", operation = "新增", description = "新增费用")
    public Result<Void> create(
            @Valid @RequestBody ExpenseCreateRequest req
    ) {

        expenseService.create(req);

        return Result.success();

    }


    @PutMapping("/{id}")
    @OperationLog(module = "费用管理", operation = "修改", description = "修改费用")
    public Result<Void> update(
            @PathVariable Long id,
            @RequestBody ExpenseUpdateRequest req
    ) {

        expenseService.update(id, req);

        return Result.success();

    }


    @DeleteMapping("/{id}")
    @OperationLog(module = "费用管理", operation = "删除", description = "删除费用")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        expenseService.delete(id);

        return Result.success();

    }


    @PostMapping("/{id}/approve")
    @OperationLog(module = "费用管理", operation = "审批", description = "审批费用")
    public Result<Void> approve(
            @PathVariable Long id
    ) {

        expenseService.approve(id);

        return Result.success();

    }


    @PostMapping("/{id}/reject")
    @OperationLog(module = "费用管理", operation = "驳回", description = "驳回费用")
    public Result<Void> reject(
            @PathVariable Long id
    ) {

        expenseService.reject(id);

        return Result.success();

    }


    @PostMapping("/{id}/pay")
    @OperationLog(module = "费用管理", operation = "付款", description = "确认付款")
    public Result<Void> pay(
            @PathVariable Long id
    ) {

        expenseService.pay(id);

        return Result.success();

    }

}
