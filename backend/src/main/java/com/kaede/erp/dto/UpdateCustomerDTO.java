package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UpdateCustomerDTO {


    private Long id;


    @NotBlank(message = "客户名称不能为空")
    private String name;


    private String phone;


    private String address;


    private String level;

}
