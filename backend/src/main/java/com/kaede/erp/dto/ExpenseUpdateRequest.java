package com.kaede.erp.dto;


import lombok.Data;

import java.math.BigDecimal;


@Data
public class ExpenseUpdateRequest {


    private String employeeName;


    private String department;


    private String type;


    private BigDecimal amount;


    private String description;


    private String attachmentUrl;


    private String remark;

}
