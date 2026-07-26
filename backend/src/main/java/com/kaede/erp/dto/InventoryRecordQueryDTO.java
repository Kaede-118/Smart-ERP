package com.kaede.erp.dto;


import lombok.Data;


@Data
public class InventoryRecordQueryDTO {


    private Long productId;


    private String type;


    private Integer page = 1;


    private Integer size = 10;

}
