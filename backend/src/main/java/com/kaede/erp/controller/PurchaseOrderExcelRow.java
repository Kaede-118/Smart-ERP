package com.kaede.erp.controller;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class PurchaseOrderExcelRow {


    @ExcelProperty("采购单号")
    private String orderNo;


    @ExcelProperty("供应商")
    private String supplierName;


    @ExcelProperty("内容")
    private String itemNames;


    @ExcelProperty("金额")
    private BigDecimal totalAmount;


    @ExcelProperty("状态")
    private String status;


    @ExcelProperty("创建人")
    private String creatorName;


    @ExcelProperty("创建时间")
    private String createTime;

}
