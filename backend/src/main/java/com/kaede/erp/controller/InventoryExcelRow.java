package com.kaede.erp.controller;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


@Data
public class InventoryExcelRow {


    @ExcelProperty("商品名称")
    private String productName;


    @ExcelProperty("商品编码")
    private String productCode;


    @ExcelProperty("分类")
    private String categoryName;


    @ExcelProperty("当前库存")
    private Integer quantity;


    @ExcelProperty("预警值")
    private Integer warningValue;

}
