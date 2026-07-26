package com.kaede.erp.dto;


import lombok.Data;


@Data
public class RoleQueryDTO {


    private String keyword;


    private Integer page = 1;


    private Integer size = 10;

}
