package com.kaede.erp.controller;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateSalesOrderDTO;
import com.kaede.erp.service.SalesOrderService;
import com.kaede.erp.vo.SalesOrderVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/sales")
public class SalesOrderController {


    private final SalesOrderService salesOrderService;


    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }


    @PostMapping("/orders")
    public Result<SalesOrderVO> create(
            @Valid @RequestBody CreateSalesOrderDTO dto
    ) {

        Long creatorId = UserContext.getUserId();

        return Result.success(
                salesOrderService.create(dto, creatorId)
        );

    }


    @GetMapping("/orders")
    public Result<List<SalesOrderVO>> list() {

        return Result.success(
                salesOrderService.list()
        );

    }


    @GetMapping("/orders/{id}")
    public Result<SalesOrderVO> getOrder(
            @PathVariable Long id
    ) {

        return Result.success(
                salesOrderService.getOrder(id)
        );

    }


    @PostMapping("/orders/{id}/complete")
    public Result<Void> complete(
            @PathVariable Long id
    ) {

        Long operatorId = UserContext.getUserId();

        salesOrderService.complete(id, operatorId);

        return Result.success();

    }

}
