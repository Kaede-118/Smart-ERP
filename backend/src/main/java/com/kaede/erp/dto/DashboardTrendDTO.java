package com.kaede.erp.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class DashboardTrendDTO {


    private LocalDate date;

    private BigDecimal purchaseAmount;

    private BigDecimal saleAmount;

}
