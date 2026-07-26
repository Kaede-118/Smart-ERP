package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UpdateSupplierDTO {


    private Long id;


    @NotBlank(message = "供应商名称不能为空")
    private String name;


    private String contact;


    private String phone;


    private String address;


    private Integer status;

}
