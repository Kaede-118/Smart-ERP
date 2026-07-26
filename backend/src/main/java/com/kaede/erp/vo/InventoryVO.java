package com.kaede.erp.vo;


import lombok.Data;

import java.util.Map;


@Data
public class InventoryVO {


    private Long productId;


    private String productName;


    private String productCode;


    private String categoryName;


    private Integer quantity;


    private Integer warningValue;


    private Long warehouseId;


    @SuppressWarnings("unchecked")
    public static InventoryVO fromMap(Map<String, Object> map) {

        if (map == null) {
            return null;
        }

        InventoryVO vo = new InventoryVO();

        vo.setProductId(toLong(map.get("product_id")));
        vo.setProductName((String) map.get("product_name"));
        vo.setProductCode((String) map.get("product_code"));
        vo.setCategoryName((String) map.get("category_name"));
        vo.setQuantity((Integer) map.get("quantity"));
        vo.setWarningValue((Integer) map.get("warning_value"));
        vo.setWarehouseId(toLong(map.get("warehouse_id")));

        return vo;
    }


    private static Long toLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

}
