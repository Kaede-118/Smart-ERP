package com.kaede.erp.vo;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ProductCategoryVO {


    private Long id;


    private String name;


    private Long parentId;


    private Integer status;


    private LocalDateTime createTime;

}
