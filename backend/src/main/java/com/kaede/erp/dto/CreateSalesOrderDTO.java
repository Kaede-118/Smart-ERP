package com.kaede.erp.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class CreateSalesOrderDTO {


    @NotNull(message = "客户不能为空")
    private Long customerId;


    @Valid
    @NotEmpty(message = "销售明细不能为空")
    private List<SalesItemDTO> items;


    @Data
    public static class SalesItemDTO {


        @NotNull(message = "商品ID不能为空")
        private Long productId;


        @NotNull(message = "数量不能为空")
        private Integer quantity;


        @NotNull(message = "单价不能为空")
        private BigDecimal price;

    }

}
