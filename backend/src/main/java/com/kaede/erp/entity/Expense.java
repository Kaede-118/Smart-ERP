package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@TableName("expense")
public class Expense {


    private Long id;


    private String expenseNo;


    private Long employeeId;


    private String employeeName;


    private String department;


    private String type;


    private BigDecimal amount;


    private String description;


    private String status;


    private String attachmentUrl;


    private String remark;


    private Long createBy;


    private LocalDateTime createTime;


    private LocalDateTime approveTime;


    private LocalDateTime payTime;


    private LocalDateTime updateTime;

}
