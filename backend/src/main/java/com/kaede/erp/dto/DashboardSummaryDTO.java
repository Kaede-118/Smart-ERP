package com.kaede.erp.dto;


import lombok.Data;

import java.math.BigDecimal;


@Data
public class DashboardSummaryDTO {


    private long productCount;

    private long customerCount;

    private long supplierCount;

    private long inventoryQuantity;

    private BigDecimal todayPurchaseAmount;

    private BigDecimal todaySaleAmount;

    private long lowStockCount;

}
