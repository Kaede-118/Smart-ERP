package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class UpdateProductDTO {


    private Long id;


    private Long categoryId;


    @NotBlank(message = "商品名称不能为空")
    private String name;


    private String coverUrl;


    private BigDecimal costPrice;


    private BigDecimal salePrice;


    private String unit;


    private Integer status;


    private String description;

}
