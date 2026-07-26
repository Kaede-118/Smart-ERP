package com.kaede.erp.vo;


import lombok.Data;

import java.util.Map;


@Data
public class SupplierVO {


    private Long id;


    private String name;


    private String contact;


    private String phone;


    private String address;


    private Integer status;


    public static SupplierVO fromEntity(Object entity) {
        return null; // handled by converter
    }

}
