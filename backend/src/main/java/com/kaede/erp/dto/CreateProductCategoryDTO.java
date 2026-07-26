package com.kaede.erp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateProductCategoryDTO {


    @NotBlank(message = "分类名称不能为空")
    private String name;


    private Long parentId;


    private Integer status = 1;

}
