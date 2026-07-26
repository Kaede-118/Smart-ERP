package com.kaede.erp.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Data
public class PurchaseOrderVO {


    private Long id;


    private String orderNo;


    private Long supplierId;


    private String supplierName;


    private BigDecimal totalAmount;


    private String status;


    private String creatorName;


    private String itemNames;


    private Long itemCount;


    private LocalDateTime createTime;


    private List<PurchaseItemVO> items;


    @SuppressWarnings("unchecked")
    public static PurchaseOrderVO fromMap(Map<String, Object> map) {

        if (map == null) {
            return null;
        }

        PurchaseOrderVO vo = new PurchaseOrderVO();

        vo.setId(toLong(map.get("id")));
        vo.setOrderNo((String) map.get("order_no"));
        vo.setSupplierId(toLong(map.get("supplier_id")));
        vo.setSupplierName((String) map.get("supplier_name"));
        vo.setTotalAmount((BigDecimal) map.get("total_amount"));
        vo.setStatus((String) map.get("status"));
        vo.setCreatorName((String) map.get("creator_name"));
        vo.setItemNames((String) map.get("item_names"));
        vo.setItemCount(toLong(map.get("item_count")));
        vo.setCreateTime((LocalDateTime) map.get("create_time"));

        return vo;
    }


    private static Long toLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

}
