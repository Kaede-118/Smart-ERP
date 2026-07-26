package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CreateProductDTO {


    private Long categoryId;


    @NotBlank(message = "商品名称不能为空")
    private String name;


    @NotBlank(message = "商品编码不能为空")
    private String code;


    private String coverUrl;


    private BigDecimal costPrice;


    private BigDecimal salePrice;


    private String unit;


    private Integer status = 1;


    private String description;

}
