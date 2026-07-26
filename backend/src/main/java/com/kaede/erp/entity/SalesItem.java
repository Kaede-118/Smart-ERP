package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@TableName("sales_item")
public class SalesItem {


    private Long id;


    private Long orderId;


    private Long productId;


    private Integer quantity;


    private BigDecimal price;


    private BigDecimal amount;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
