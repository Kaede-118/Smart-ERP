package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class UpdateUserDTO {


    @NotNull(message = "用户ID不能为空")
    private Long id;


    @NotBlank(message = "用户名不能为空")
    private String username;


    @NotBlank(message = "昵称不能为空")
    private String nickname;


    @NotNull(message = "状态不能为空")
    private Integer status;

}
