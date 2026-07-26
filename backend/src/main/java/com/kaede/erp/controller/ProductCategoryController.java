package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateProductCategoryDTO;
import com.kaede.erp.dto.UpdateProductCategoryDTO;
import com.kaede.erp.service.ProductCategoryService;
import com.kaede.erp.vo.ProductCategoryVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product-categories")
public class ProductCategoryController {


    private final ProductCategoryService productCategoryService;


    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }


    @PostMapping
    public Result<ProductCategoryVO> create(
            @Valid @RequestBody CreateProductCategoryDTO dto
    ) {

        return Result.success(
                productCategoryService.create(dto)
        );

    }


    @GetMapping
    public Result<List<ProductCategoryVO>> list() {

        return Result.success(
                productCategoryService.list()
        );

    }


    @PutMapping("/{id}")
    public Result<ProductCategoryVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductCategoryDTO dto
    ) {

        dto.setId(id);

        return Result.success(
                productCategoryService.update(dto)
        );

    }


    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        productCategoryService.delete(id);

        return Result.success();

    }

}
