package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateSupplierDTO;
import com.kaede.erp.dto.UpdateSupplierDTO;
import com.kaede.erp.service.SupplierService;
import com.kaede.erp.vo.SupplierVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {


    private final SupplierService supplierService;


    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }


    @PostMapping
    public Result<SupplierVO> create(
            @Valid @RequestBody CreateSupplierDTO dto
    ) {

        return Result.success(
                supplierService.create(dto)
        );

    }


    @GetMapping
    public Result<List<SupplierVO>> list() {

        return Result.success(
                supplierService.list()
        );

    }


    @PutMapping("/{id}")
    public Result<SupplierVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSupplierDTO dto
    ) {

        dto.setId(id);

        return Result.success(
                supplierService.update(dto)
        );

    }


    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        supplierService.delete(id);

        return Result.success();

    }

}
