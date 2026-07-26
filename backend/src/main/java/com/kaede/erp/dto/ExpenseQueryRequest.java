package com.kaede.erp.dto;


import lombok.Data;


@Data
public class ExpenseQueryRequest {


    private String keyword;


    private String type;


    private String status;


    private Integer page = 1;


    private Integer size = 10;

}
