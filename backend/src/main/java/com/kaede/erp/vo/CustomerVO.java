package com.kaede.erp.vo;


import lombok.Data;

import java.util.Map;


@Data
public class CustomerVO {


    private Long id;


    private String name;


    private String phone;


    private String address;


    private String level;


    public static CustomerVO fromMap(Map<String, Object> map) {
        return null;
    }

}
