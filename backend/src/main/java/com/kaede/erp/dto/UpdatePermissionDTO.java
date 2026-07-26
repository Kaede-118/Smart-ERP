package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdatePermissionDTO {


    private Long id;


    @NotBlank(message = "权限名称不能为空")
    private String name;


    private String type = "button";


    private Long parentId;

}
