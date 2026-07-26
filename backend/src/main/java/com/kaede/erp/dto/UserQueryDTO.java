package com.kaede.erp.dto;


import lombok.Data;


@Data
public class UserQueryDTO {


    private String username;


    private Integer status;


    private Integer page = 1;


    private Integer size = 10;

}