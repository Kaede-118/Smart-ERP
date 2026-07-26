package com.kaede.erp.controller;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ProductExcelRow {


    @ExcelProperty("商品名称")
    private String name;


    @ExcelProperty("商品编码")
    private String code;


    @ExcelProperty("成本价")
    private BigDecimal costPrice;


    @ExcelProperty("售价")
    private BigDecimal salePrice;


    @ExcelProperty("单位")
    private String unit;


    @ExcelProperty("状态")
    private Integer status;

}
