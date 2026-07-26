package com.kaede.erp.controller;


import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.InventoryAdjustDTO;
import com.kaede.erp.dto.InventoryQueryDTO;
import com.kaede.erp.dto.InventoryRecordQueryDTO;
import com.kaede.erp.service.InventoryService;
import com.kaede.erp.vo.InventoryRecordVO;
import com.kaede.erp.vo.InventoryVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inventory")
public class InventoryController {


    private final InventoryService inventoryService;


    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }


    @GetMapping("/list")
    public Result<List<InventoryVO>> list(
            InventoryQueryDTO dto
    ) {

        return Result.success(
                inventoryService.list(dto.getKeyword(), dto.getCategoryId())
        );

    }


    @GetMapping("/{productId}")
    public Result<InventoryVO> getByProduct(
            @PathVariable Long productId
    ) {

        return Result.success(
                inventoryService.getByProductId(productId)
        );

    }


    @PostMapping("/adjust")
    public Result<Void> adjust(
            @Valid @RequestBody InventoryAdjustDTO dto
    ) {

        Long operatorId = UserContext.getUserId();

        inventoryService.adjust(
                dto.getProductId(),
                dto.getChangeQty(),
                dto.getRemark(),
                operatorId
        );

        return Result.success();

    }


    @GetMapping("/records")
    public Result<List<InventoryRecordVO>> records(
            InventoryRecordQueryDTO dto
    ) {

        return Result.success(
                inventoryService.getRecords(dto.getProductId(), dto.getType())
        );

    }

}
