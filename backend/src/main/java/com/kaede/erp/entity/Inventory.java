package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@TableName("inventory")
public class Inventory {


    private Long id;


    private Long productId;


    private Integer quantity;


    private Integer warningValue;


    private Long warehouseId;


    private LocalDateTime updateTime;

}
