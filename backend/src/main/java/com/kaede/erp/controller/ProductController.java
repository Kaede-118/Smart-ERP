package com.kaede.erp.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kaede.erp.common.result.Result;
import com.kaede.erp.dto.CreateProductDTO;
import com.kaede.erp.dto.ProductQueryDTO;
import com.kaede.erp.dto.UpdateProductDTO;
import com.kaede.erp.service.ProductService;
import com.kaede.erp.vo.ProductVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping
    public Result<ProductVO> create(
            @Valid @RequestBody CreateProductDTO dto
    ) {

        return Result.success(
                productService.create(dto)
        );

    }


    @GetMapping("/list")
    public Result<Page<ProductVO>> list(
            ProductQueryDTO dto
    ) {

        return Result.success(
                productService.list(dto)
        );

    }


    @GetMapping("/{id}")
    public Result<ProductVO> getProduct(
            @PathVariable Long id
    ) {

        return Result.success(
                productService.getProduct(id)
        );

    }


    @PutMapping("/{id}")
    public Result<ProductVO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDTO dto
    ) {

        dto.setId(id);

        return Result.success(
                productService.update(dto)
        );

    }


    @DeleteMapping("/{id}")
    public Result<Void> delete(
            @PathVariable Long id
    ) {

        productService.delete(id);

        return Result.success();

    }

}
