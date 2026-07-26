package com.kaede.erp.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class CreatePurchaseOrderDTO {


    @NotNull(message = "供应商不能为空")
    private Long supplierId;


    @Valid
    @NotEmpty(message = "采购明细不能为空")
    private List<PurchaseItemDTO> items;


    @Data
    public static class PurchaseItemDTO {


        @NotNull(message = "商品ID不能为空")
        private Long productId;


        @NotNull(message = "数量不能为空")
        private Integer quantity;


        @NotNull(message = "单价不能为空")
        private BigDecimal price;

    }

}
