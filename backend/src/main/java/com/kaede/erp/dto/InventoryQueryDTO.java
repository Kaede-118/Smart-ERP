package com.kaede.erp.dto;


import lombok.Data;


@Data
public class InventoryQueryDTO {


    private String keyword;


    private Long categoryId;


    private Integer page = 1;


    private Integer size = 10;

}
