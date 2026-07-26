package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreatePermissionDTO {


    @NotBlank(message = "权限名称不能为空")
    private String name;


    @NotBlank(message = "权限编码不能为空")
    private String code;


    private String type = "button";


    private Long parentId;

}
