package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("inventory_record")
public class InventoryRecord {


    private Long id;


    private Long productId;


    private Integer changeQty;


    private Integer beforeQty;


    private Integer afterQty;


    private String type;


    private String businessType;


    private Long businessId;


    private String remark;


    private Long operatorId;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
