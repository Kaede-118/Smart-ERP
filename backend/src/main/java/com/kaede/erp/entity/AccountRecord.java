package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("account_record")
public class AccountRecord {


    private Long id;


    private String changeAmount;


    private String beforeBalance;


    private String afterBalance;


    private String type;


    private String businessType;


    private Long businessId;


    private String remark;


    private Long operatorId;


    private LocalDateTime createTime;

}
