package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


@Data
@TableName("purchase_item")
public class PurchaseItem {


    private Long id;


    private Long orderId;


    private Long productId;


    private Integer quantity;


    private BigDecimal price;


    private BigDecimal amount;

}
