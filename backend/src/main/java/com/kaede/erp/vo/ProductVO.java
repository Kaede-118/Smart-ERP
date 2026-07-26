package com.kaede.erp.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ProductVO {


    private Long id;


    private Long categoryId;


    private String name;


    private String code;


    private String coverUrl;


    private BigDecimal costPrice;


    private BigDecimal salePrice;


    private String unit;


    private Integer status;


    private String description;


    private LocalDateTime createTime;

}
