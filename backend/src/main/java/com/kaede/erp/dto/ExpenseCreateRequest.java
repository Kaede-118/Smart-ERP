package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ExpenseCreateRequest {


    private Long employeeId;


    private String employeeName;


    private String department;


    @NotBlank(message = "费用类型不能为空")
    private String type;


    @NotNull(message = "金额不能为空")
    private BigDecimal amount;


    private String description;


    private String attachmentUrl;


    private String remark;

}
