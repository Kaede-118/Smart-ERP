package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("account_transaction")
public class AccountTransaction {


    private Long id;


    private String changeAmount;


    private String beforeBalance;


    private String afterBalance;


    private String type;


    private String changeType;


    private String businessType;


    private Long businessId;


    private String remark;


    private Long operatorId;


    private String createTime;

}
