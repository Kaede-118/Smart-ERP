package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateRoleDTO {


    private Long id;


    @NotBlank(message = "角色名称不能为空")
    private String roleName;


    private String description;

}
