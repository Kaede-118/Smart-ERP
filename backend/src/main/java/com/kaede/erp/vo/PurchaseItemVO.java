package com.kaede.erp.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;


@Data
public class PurchaseItemVO {


    private Long id;


    private Long productId;


    private String productName;


    private String productCode;


    private Integer quantity;


    private BigDecimal price;


    private BigDecimal amount;


    @SuppressWarnings("unchecked")
    public static PurchaseItemVO fromMap(Map<String, Object> map) {

        if (map == null) {
            return null;
        }

        PurchaseItemVO vo = new PurchaseItemVO();

        vo.setId(toLong(map.get("id")));
        vo.setProductId(toLong(map.get("product_id")));
        vo.setProductName((String) map.get("product_name"));
        vo.setProductCode((String) map.get("product_code"));
        vo.setQuantity((Integer) map.get("quantity"));
        vo.setPrice((BigDecimal) map.get("price"));
        vo.setAmount((BigDecimal) map.get("amount"));

        return vo;
    }


    private static Long toLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

}
