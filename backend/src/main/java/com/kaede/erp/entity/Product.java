package com.kaede.erp.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@TableName("product")
public class Product {


    private Long id;


    private Long categoryId;


    private String name;


    private String code;


    private String coverUrl;


    private BigDecimal costPrice;


    private BigDecimal salePrice;


    private String unit;


    private Integer status;


    private String description;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
