package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@TableName("company_account")
public class CompanyAccount {


    private Long id;


    private String accountName;


    private BigDecimal balance;


    private LocalDateTime updateTime;

}
