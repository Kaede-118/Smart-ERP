package com.kaede.erp.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class InventoryAdjustDTO {


    @NotNull(message = "商品ID不能为空")
    private Long productId;


    @NotNull(message = "变更为不能为空")
    private Integer changeQty;


    private String remark;

}
