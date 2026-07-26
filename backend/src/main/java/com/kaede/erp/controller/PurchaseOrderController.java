package com.kaede.erp.controller;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreatePurchaseOrderDTO;
import com.kaede.erp.service.PurchaseOrderService;
import com.kaede.erp.vo.PurchaseOrderVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/purchase")
public class PurchaseOrderController {


    private final PurchaseOrderService purchaseOrderService;


    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }


    @PostMapping("/orders")
    public Result<PurchaseOrderVO> create(
            @Valid @RequestBody CreatePurchaseOrderDTO dto
    ) {

        Long creatorId = UserContext.getUserId();

        return Result.success(
                purchaseOrderService.create(dto, creatorId)
        );

    }


    @GetMapping("/orders")
    public Result<List<PurchaseOrderVO>> list() {

        return Result.success(
                purchaseOrderService.list()
        );

    }


    @GetMapping("/orders/{id}")
    public Result<PurchaseOrderVO> getOrder(
            @PathVariable Long id
    ) {

        return Result.success(
                purchaseOrderService.getOrder(id)
        );

    }


    @PostMapping("/orders/{id}/receive")
    public Result<Void> receive(
            @PathVariable Long id
    ) {

        Long operatorId = UserContext.getUserId();

        purchaseOrderService.receive(id, operatorId);

        return Result.success();

    }

}
