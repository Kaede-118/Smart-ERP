package com.kaede.erp.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class ExpenseSummaryDTO {


    private BigDecimal monthExpense;

    private long pendingCount;

    private long paidCount;

    private BigDecimal todayExpense;

    private BigDecimal companyBalance;

}
